package com.airesume.analyzer.controller;

import com.airesume.analyzer.dto.ResumeAnalysisResponseDto;
import com.airesume.analyzer.entity.ResumeAnalysis;
import com.airesume.analyzer.service.ResumeAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * ResumeAnalysisController
 * Manages multipart resume uploads, triggers AI matching, and exposes user-scoped analysis reports.
 */
@RestController
@RequestMapping("/api/analyses")
public class ResumeAnalysisController {

    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeAnalysisController(ResumeAnalysisService resumeAnalysisService) {
        this.resumeAnalysisService = resumeAnalysisService;
    }

    /**
     * Upload resume PDF and job description to perform AI ATS match evaluation.
     * POST /api/analyses (multipart/form-data)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeAnalysisResponseDto> analyzeResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("targetRole") String targetRole,
            @RequestParam("jobDescription") String jobDescription,
            @AuthenticationPrincipal UserDetails userDetails) {

        ResumeAnalysis analysis = resumeAnalysisService.analyzeAndSave(
                file,
                targetRole,
                jobDescription,
                userDetails.getUsername()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ResumeAnalysisResponseDto.fromEntity(analysis));
    }

    /**
     * Retrieve all past resume evaluations belonging to the authenticated user.
     * GET /api/analyses
     */
    @GetMapping
    public ResponseEntity<List<ResumeAnalysisResponseDto>> getUserAnalyses(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<ResumeAnalysis> analyses = resumeAnalysisService.getUserAnalyses(userDetails.getUsername());
        List<ResumeAnalysisResponseDto> responseDtos = analyses.stream()
                .map(ResumeAnalysisResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Retrieve a specific evaluation report ensuring ownership.
     * GET /api/analyses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResumeAnalysisResponseDto> getAnalysisById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        ResumeAnalysis analysis = resumeAnalysisService.getAnalysisByIdAndUser(id, userDetails.getUsername());
        return ResponseEntity.ok(ResumeAnalysisResponseDto.fromEntity(analysis));
    }

    /**
     * Delete an evaluation report scoped to the authenticated user.
     * DELETE /api/analyses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAnalysis(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        resumeAnalysisService.deleteAnalysis(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Resume analysis report deleted successfully."));
    }
}
