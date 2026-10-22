package com.airesume.analyzer.dto;

import com.airesume.analyzer.entity.ResumeAnalysis;
import java.time.LocalDateTime;

/**
 * ResumeAnalysisResponseDto
 * Data transfer object returned by the REST API for resume evaluations.
 */
public class ResumeAnalysisResponseDto {

    private Long id;
    private String originalFilename;
    private String targetRole;
    private String jobDescription;
    private Integer overallScore;
    private String qualityAssessment;
    private String analysisJson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ResumeAnalysisResponseDto() {
    }

    public static ResumeAnalysisResponseDto fromEntity(ResumeAnalysis entity) {
        if (entity == null) return null;
        ResumeAnalysisResponseDto dto = new ResumeAnalysisResponseDto();
        dto.setId(entity.getId());
        dto.setOriginalFilename(entity.getOriginalFilename());
        dto.setTargetRole(entity.getTargetRole());
        dto.setJobDescription(entity.getJobDescription());
        dto.setOverallScore(entity.getOverallScore());
        dto.setQualityAssessment(entity.getQualityAssessment());
        dto.setAnalysisJson(entity.getAnalysisJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public String getQualityAssessment() {
        return qualityAssessment;
    }

    public void setQualityAssessment(String qualityAssessment) {
        this.qualityAssessment = qualityAssessment;
    }

    public String getAnalysisJson() {
        return analysisJson;
    }

    public void setAnalysisJson(String analysisJson) {
        this.analysisJson = analysisJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
