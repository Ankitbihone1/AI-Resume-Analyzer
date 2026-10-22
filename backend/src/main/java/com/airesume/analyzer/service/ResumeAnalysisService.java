package com.airesume.analyzer.service;

import com.airesume.analyzer.dto.AnalysisReportDto;
import com.airesume.analyzer.entity.ResumeAnalysis;
import com.airesume.analyzer.entity.User;
import com.airesume.analyzer.exception.ResourceNotFoundException;
import com.airesume.analyzer.repository.ResumeAnalysisRepository;
import com.airesume.analyzer.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ResumeAnalysisService
 * Orchestrates PDF text extraction, AI evaluation, MySQL persistence, and user security scoping.
 */
@Service
public class ResumeAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeAnalysisService.class);

    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final UserRepository userRepository;
    private final PdfTextExtractionService pdfTextExtractionService;
    private final AiClientService aiClientService;
    private final ObjectMapper objectMapper;

    public ResumeAnalysisService(ResumeAnalysisRepository resumeAnalysisRepository,
                                 UserRepository userRepository,
                                 PdfTextExtractionService pdfTextExtractionService,
                                 AiClientService aiClientService,
                                 ObjectMapper objectMapper) {
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.userRepository = userRepository;
        this.pdfTextExtractionService = pdfTextExtractionService;
        this.aiClientService = aiClientService;
        this.objectMapper = objectMapper;
    }

    /**
     * Process resume upload, run AI evaluation, and persist user-scoped report in MySQL.
     */
    @Transactional
    public ResumeAnalysis analyzeAndSave(MultipartFile file, String targetRole, String jobDescription, String userEmail) {
        if (targetRole == null || targetRole.isBlank()) {
            throw new IllegalArgumentException("Target job title or role cannot be empty.");
        }
        if (jobDescription == null || jobDescription.trim().length() < 20) {
            throw new IllegalArgumentException("Job description must contain at least 20 characters.");
        }

        User user = userRepository.findByEmail(userEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        // 1. Extract text using Apache PDFBox
        String extractedText = pdfTextExtractionService.extractText(file);
        logger.info("Extracted {} characters from resume '{}' for user '{}'",
                extractedText.length(), file.getOriginalFilename(), userEmail);

        // 2. Perform AI evaluation
        AnalysisReportDto report = aiClientService.analyzeResume(extractedText, jobDescription.trim(), targetRole.trim());

        // 3. Serialize report to JSON string
        String analysisJson;
        try {
            analysisJson = objectMapper.writeValueAsString(report);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize analysis report to JSON", e);
            analysisJson = "{}";
        }

        // 4. Save to MySQL
        ResumeAnalysis analysis = new ResumeAnalysis(
                user,
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "Resume.pdf",
                targetRole.trim(),
                jobDescription.trim(),
                extractedText,
                report.getOverallScore(),
                report.getQualityAssessment(),
                analysisJson
        );

        ResumeAnalysis savedAnalysis = resumeAnalysisRepository.save(analysis);
        logger.info("Saved resume analysis ID {} with overall score {} for user '{}'",
                savedAnalysis.getId(), savedAnalysis.getOverallScore(), userEmail);

        return savedAnalysis;
    }

    /**
     * Retrieve all evaluations belonging to the authenticated user.
     */
    @Transactional(readOnly = true)
    public List<ResumeAnalysis> getUserAnalyses(String userEmail) {
        User user = userRepository.findByEmail(userEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        return resumeAnalysisRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    /**
     * Retrieve a specific analysis report ensuring strict user-scoping.
     */
    @Transactional(readOnly = true)
    public ResumeAnalysis getAnalysisByIdAndUser(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        return resumeAnalysisRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Analysis report #" + id + " not found or unauthorized"));
    }

    /**
     * Delete an analysis report owned by the authenticated user.
     */
    @Transactional
    public void deleteAnalysis(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        ResumeAnalysis analysis = resumeAnalysisRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Analysis report #" + id + " not found or unauthorized"));

        resumeAnalysisRepository.delete(analysis);
        logger.info("Deleted analysis ID {} for user '{}'", id, userEmail);
    }
}
