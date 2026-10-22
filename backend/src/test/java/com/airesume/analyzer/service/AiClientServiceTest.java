package com.airesume.analyzer.service;

import com.airesume.analyzer.dto.AnalysisReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AiClientServiceTest {

    private AiClientService aiClientService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        aiClientService = new AiClientService(restTemplate, objectMapper);
    }

    @Test
    @DisplayName("Should successfully construct formatted prompt with target role, job description, and resume text")
    void testBuildPrompt() {
        String prompt = aiClientService.buildPrompt(
                "Experience in Spring Boot and React.",
                "Seeking Java Full Stack Developer proficient in Spring Boot and Docker.",
                "Java Full Stack Developer"
        );

        assertNotNull(prompt);
        assertTrue(prompt.contains("Java Full Stack Developer"));
        assertTrue(prompt.contains("Spring Boot"));
        assertTrue(prompt.contains("Docker"));
        assertTrue(prompt.contains("overallScore"));
    }

    @Test
    @DisplayName("Should parse JSON even when wrapped in markdown code fences")
    void testExtractAndParseJsonDto_WithMarkdownFences() {
        String jsonPayload = """
        ```json
        {
          "overallScore": 88,
          "qualityAssessment": "Solid candidate profile.",
          "matchSummary": "Matches 88% of core skills.",
          "matchingSkills": ["Java", "Spring Boot", "React"],
          "missingSkills": ["AWS"],
          "keywordsFound": ["REST", "SQL"],
          "keywordsMissing": ["Kubernetes"],
          "strengths": ["Strong backend skills"],
          "improvements": ["Add cloud certifications"],
          "bulletPointSuggestions": [
            {
              "original": "Built APIs in Spring Boot.",
              "suggested": "Designed 10+ REST APIs in Spring Boot, decreasing latency by 15%."
            }
          ]
        }
        ```
        """;

        AnalysisReportDto dto = aiClientService.extractAndParseJsonDto(jsonPayload);

        assertNotNull(dto);
        assertEquals(88, dto.getOverallScore());
        assertEquals("Solid candidate profile.", dto.getQualityAssessment());
        assertEquals(3, dto.getMatchingSkills().size());
        assertEquals(1, dto.getBulletPointSuggestions().size());
        assertEquals("Built APIs in Spring Boot.", dto.getBulletPointSuggestions().get(0).getOriginal());
    }

    @Test
    @DisplayName("Should perform deterministic algorithmic evaluation when external AI is offline")
    void testPerformAlgorithmicEvaluation() {
        String resume = """
        Alex Morgan
        Skills: Java, Spring Boot, React, MySQL, Git, Docker, REST
        Experience:
        - Developed REST APIs using Spring Boot and connected to MySQL.
        """;

        String job = """
        Looking for Junior Java Developer with Java, Spring Boot, React, MySQL, Docker, and Kubernetes.
        """;

        AnalysisReportDto dto = aiClientService.performAlgorithmicEvaluation(resume, job, "Junior Java Developer");

        assertNotNull(dto);
        assertTrue(dto.getOverallScore() >= 50 && dto.getOverallScore() <= 100);
        assertTrue(dto.getMatchingSkills().contains("Java"));
        assertTrue(dto.getMatchingSkills().contains("Spring Boot"));
        assertTrue(dto.getMissingSkills().contains("Kubernetes"));
        assertFalse(dto.getBulletPointSuggestions().isEmpty());
    }
}
