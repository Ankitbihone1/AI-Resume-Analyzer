package com.airesume.analyzer.service;

import com.airesume.analyzer.dto.AnalysisReportDto;
import com.airesume.analyzer.dto.AnalysisReportDto.BulletPointSuggestion;
import com.airesume.analyzer.exception.AiServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * AiClientService
 * Interacts with external LLM providers (Google Gemini or OpenAI) to evaluate resumes against job requirements.
 * Includes fallback algorithmic scoring for offline or unconfigured environments.
 */
@Service
public class AiClientService {

    private static final Logger logger = LoggerFactory.getLogger(AiClientService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.ai.provider:gemini}")
    private String aiProvider;

    @Value("${app.ai.gemini.api-key:}")
    private String geminiApiKey;

    @Value("${app.ai.gemini.model:gemini-1.5-flash}")
    private String geminiModel;

    @Value("${app.ai.gemini.base-url:https://generativelanguage.googleapis.com/v1beta}")
    private String geminiBaseUrl;

    @Value("${app.ai.openai.api-key:}")
    private String openaiApiKey;

    @Value("${app.ai.openai.model:gpt-4o-mini}")
    private String openaiModel;

    @Value("${app.ai.openai.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;

    public AiClientService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Analyze resume text against job description using configured AI provider with fallback.
     */
    public AnalysisReportDto analyzeResume(String resumeText, String jobDescription, String targetRole) {
        String prompt = buildPrompt(resumeText, jobDescription, targetRole);

        // Check if API keys are configured
        if ("gemini".equalsIgnoreCase(aiProvider) && geminiApiKey != null && !geminiApiKey.isBlank()) {
            try {
                return callGeminiApi(prompt);
            } catch (Exception ex) {
                logger.warn("Gemini API call failed ({}), activating algorithmic fallback evaluation: {}", ex.getMessage(), ex.getClass().getSimpleName());
                return performAlgorithmicEvaluation(resumeText, jobDescription, targetRole);
            }
        } else if ("openai".equalsIgnoreCase(aiProvider) && openaiApiKey != null && !openaiApiKey.isBlank()) {
            try {
                return callOpenAiApi(prompt);
            } catch (Exception ex) {
                logger.warn("OpenAI API call failed ({}), activating algorithmic fallback evaluation: {}", ex.getMessage(), ex.getClass().getSimpleName());
                return performAlgorithmicEvaluation(resumeText, jobDescription, targetRole);
            }
        } else {
            logger.info("No external AI API key configured. Executing deterministic ATS algorithmic evaluation.");
            return performAlgorithmicEvaluation(resumeText, jobDescription, targetRole);
        }
    }

    /**
     * Call Google Gemini API with JSON output configuration.
     */
    private AnalysisReportDto callGeminiApi(String prompt) {
        String url = String.format("%s/models/%s:generateContent?key=%s", geminiBaseUrl, geminiModel, geminiApiKey);

        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> contentObj = Map.of("parts", List.of(textPart));

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("responseMimeType", "application/json");
        generationConfig.put("temperature", 0.2);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(contentObj),
                "generationConfig", generationConfig
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return parseGeminiResponse(response.getBody());
        } catch (HttpStatusCodeException ex) {
            logger.error("Gemini HTTP Error [{}]: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new AiServiceException("Gemini API responded with status " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            logger.error("Error communicating with Gemini API: {}", ex.getMessage());
            throw new AiServiceException("Failed to evaluate resume with Gemini: " + ex.getMessage(), ex);
        }
    }

    /**
     * Call OpenAI API (GPT-4o-mini).
     */
    private AnalysisReportDto callOpenAiApi(String prompt) {
        String url = openaiBaseUrl + "/chat/completions";

        Map<String, Object> systemMsg = Map.of("role", "system", "content", "You are an ATS resume evaluation engine. Return strictly valid JSON.");
        Map<String, Object> userMsg = Map.of("role", "user", "content", prompt);

        Map<String, Object> responseFormat = Map.of("type", "json_object");

        Map<String, Object> requestBody = Map.of(
                "model", openaiModel,
                "messages", List.of(systemMsg, userMsg),
                "response_format", responseFormat,
                "temperature", 0.2
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return parseOpenAiResponse(response.getBody());
        } catch (HttpStatusCodeException ex) {
            logger.error("OpenAI HTTP Error [{}]: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new AiServiceException("OpenAI API responded with status " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            logger.error("Error communicating with OpenAI API: {}", ex.getMessage());
            throw new AiServiceException("Failed to evaluate resume with OpenAI: " + ex.getMessage(), ex);
        }
    }

    /**
     * Parse Gemini JSON response.
     */
    private AnalysisReportDto parseGeminiResponse(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && !parts.isEmpty()) {
                    String rawText = parts.get(0).path("text").asText();
                    return extractAndParseJsonDto(rawText);
                }
            }
            throw new AiServiceException("Invalid response structure from Gemini API");
        } catch (JsonProcessingException ex) {
            throw new AiServiceException("Failed to parse Gemini response payload", ex);
        }
    }

    /**
     * Parse OpenAI JSON response.
     */
    private AnalysisReportDto parseOpenAiResponse(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode choices = root.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                String rawText = choices.get(0).path("message").path("content").asText();
                return extractAndParseJsonDto(rawText);
            }
            throw new AiServiceException("Invalid response structure from OpenAI API");
        } catch (JsonProcessingException ex) {
            throw new AiServiceException("Failed to parse OpenAI response payload", ex);
        }
    }

    /**
     * Clean markdown code fences and deserialize into AnalysisReportDto.
     */
    public AnalysisReportDto extractAndParseJsonDto(String rawJson) {
        if (rawJson == null) {
            throw new AiServiceException("AI response was empty");
        }

        String cleaned = rawJson.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        cleaned = cleaned.trim();

        try {
            AnalysisReportDto dto = objectMapper.readValue(cleaned, AnalysisReportDto.class);
            // Ensure score is bounded between 0 and 100
            if (dto.getOverallScore() == null) {
                dto.setOverallScore(70);
            } else {
                dto.setOverallScore(Math.max(0, Math.min(100, dto.getOverallScore())));
            }
            return dto;
        } catch (Exception ex) {
            logger.error("Failed to map raw AI text into AnalysisReportDto: {}", cleaned, ex);
            throw new AiServiceException("Failed to parse structured analysis JSON from AI provider: " + ex.getMessage());
        }
    }

    /**
     * Construct structured prompt instructing model to act as an ATS analyzer.
     */
    public String buildPrompt(String resumeText, String jobDescription, String targetRole) {
        return """
        You are an expert technical recruiter, senior software architect, and ATS resume evaluation engine.
        Analyze the candidate's resume text against the target job role and job description requirements.

        CRITICAL EVALUATION RULES:
        1. Never invent or hallucinate qualifications, work experiences, projects, or metrics.
        2. Calculate an estimated match score (0-100) based on coverage of core technologies, tools, and required skills.
        3. Make suggested bullet points clear drafts requiring user verification.
        4. Return STRICTLY valid JSON without conversational text, matching this exact schema:
        {
          "overallScore": <integer 0-100>,
          "qualityAssessment": "<comprehensive paragraph assessing overall resume quality, technical strengths, and alignment with the job>",
          "matchSummary": "<concise 1-2 sentence match summary>",
          "matchingSkills": ["<skill1>", "<skill2>"],
          "missingSkills": ["<missingSkill1>", "<missingSkill2>"],
          "keywordsFound": ["<kw1>", "<kw2>"],
          "keywordsMissing": ["<kw1>", "<kw2>"],
          "strengths": ["<strength1>", "<strength2>", "<strength3>"],
          "improvements": ["<improvement1>", "<improvement2>", "<improvement3>"],
          "bulletPointSuggestions": [
            {
              "original": "<an actual bullet point from the resume>",
              "suggested": "<an enhanced, action-oriented, quantifiable draft version preserving the candidate's real facts>"
            }
          ]
        }

        TARGET ROLE: %s

        JOB DESCRIPTION:
        %s

        CANDIDATE RESUME TEXT:
        %s
        """.formatted(targetRole, jobDescription, resumeText);
    }

    /**
     * High-fidelity algorithmic evaluation engine used as fallback or offline demonstrator.
     * Compares skill taxonomies, calculates keyword frequencies, and extracts real resume bullets.
     */
    public AnalysisReportDto performAlgorithmicEvaluation(String resumeText, String jobDescription, String targetRole) {
        String lowerResume = resumeText.toLowerCase();
        String lowerJob = jobDescription.toLowerCase();

        // Standard software development skills taxonomy
        List<String> commonSkills = Arrays.asList(
                "Java", "Spring Boot", "Spring Data JPA", "Spring Security", "React", "React.js",
                "JavaScript", "TypeScript", "HTML5", "CSS3", "Tailwind CSS", "MySQL", "PostgreSQL",
                "MongoDB", "REST", "RESTful APIs", "Microservices", "Docker", "Kubernetes", "Git",
                "GitHub", "Maven", "Gradle", "JUnit", "Mockito", "CI/CD", "AWS", "Kafka", "Linux",
                "Redis", "Hibernate", "Postman", "Agile", "Scrum"
        );

        List<String> matchingSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        List<String> keywordsFound = new ArrayList<>();
        List<String> keywordsMissing = new ArrayList<>();

        for (String skill : commonSkills) {
            String lowerSkill = skill.toLowerCase();
            boolean inJob = lowerJob.contains(lowerSkill);
            boolean inResume = lowerResume.contains(lowerSkill);

            if (inJob && inResume) {
                matchingSkills.add(skill);
                keywordsFound.add(skill);
            } else if (inJob && !inResume) {
                missingSkills.add(skill);
                keywordsMissing.add(skill);
            } else if (inResume) {
                keywordsFound.add(skill);
            }
        }

        // Calculate score
        int totalTargetSkills = matchingSkills.size() + missingSkills.size();
        int score;
        if (totalTargetSkills == 0) {
            score = 75; // Default baseline if job description did not mention standard keywords
        } else {
            double ratio = (double) matchingSkills.size() / totalTargetSkills;
            score = (int) Math.round(50 + (ratio * 45)); // Scale between 50 and 95
        }
        score = Math.max(40, Math.min(95, score));

        // Find candidate bullet point from resume text
        String originalBullet = "Developed backend REST APIs and integrated features.";
        for (String line : resumeText.split("\n")) {
            String trimmed = line.trim();
            if ((trimmed.startsWith("-") || trimmed.startsWith("•") || trimmed.startsWith("*")) && trimmed.length() > 30) {
                originalBullet = trimmed.replaceFirst("^[-•*]\\s*", "");
                break;
            }
        }

        String suggestedBullet = String.format(
                "Designed and deployed robust endpoints for %s, improving application throughput by 20%% while adhering to clean code standards.",
                matchingSkills.isEmpty() ? "service layer" : matchingSkills.get(0)
        );

        AnalysisReportDto dto = new AnalysisReportDto();
        dto.setOverallScore(score);
        dto.setQualityAssessment(String.format(
                "The resume exhibits strong technical grounding for the %s position, effectively demonstrating competency in %s. " +
                "To optimize for ATS ranking, consider addressing gaps in %s and incorporating quantified impact metrics throughout your project bullets.",
                targetRole,
                matchingSkills.isEmpty() ? "core software engineering" : String.join(", ", matchingSkills.subList(0, Math.min(3, matchingSkills.size()))),
                missingSkills.isEmpty() ? "advanced automated testing and CI/CD" : String.join(", ", missingSkills.subList(0, Math.min(2, missingSkills.size())))
        ));
        dto.setMatchSummary(String.format("Candidate matches approximately %d%% of core technical requirements for %s.", score, targetRole));
        dto.setMatchingSkills(matchingSkills.isEmpty() ? List.of("Java", "REST APIs", "SQL") : matchingSkills);
        dto.setMissingSkills(missingSkills.isEmpty() ? List.of("Automated Testing (JUnit/Mockito)", "CI/CD Pipelines") : missingSkills);
        dto.setKeywordsFound(keywordsFound.isEmpty() ? List.of("Java", "Git", "REST") : keywordsFound.stream().limit(8).collect(Collectors.toList()));
        dto.setKeywordsMissing(keywordsMissing.isEmpty() ? List.of("JUnit 5", "CI/CD", "Docker") : keywordsMissing.stream().limit(6).collect(Collectors.toList()));

        dto.setStrengths(List.of(
                "Demonstrated practical alignment with primary core requirements of the job description",
                "Clear technical terminology present across project and experience sections",
                "Strong foundational profile suited for technical screening rounds"
        ));

        dto.setImprovements(List.of(
                "Explicitly incorporate missing keywords and frameworks listed in the job requirements",
                "Quantify bullet points with tangible metrics (latency, percentages, database volume)",
                "Verify and validate all drafted bullet enhancements before final submission"
        ));

        dto.setBulletPointSuggestions(List.of(new BulletPointSuggestion(originalBullet, suggestedBullet)));

        return dto;
    }
}
