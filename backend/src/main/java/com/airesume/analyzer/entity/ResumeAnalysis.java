package com.airesume.analyzer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ResumeAnalysis Entity
 * Stores extracted text, target role requirements, overall match scores, and structured JSON evaluation data.
 */
@Entity
@Table(name = "resume_analyses", indexes = {
    @Index(name = "idx_analyses_user_date", columnList = "user_id, created_at DESC")
})
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "target_role", nullable = false, length = 150)
    private String targetRole;

    @Column(name = "job_description", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String jobDescription;

    @Column(name = "extracted_text", columnDefinition = "MEDIUMTEXT")
    private String extractedText;

    @Column(name = "overall_score", nullable = false)
    private Integer overallScore;

    @Column(name = "quality_assessment", columnDefinition = "TEXT")
    private String qualityAssessment;

    @Column(name = "analysis_json", nullable = false, columnDefinition = "JSON")
    private String analysisJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ResumeAnalysis() {
    }

    public ResumeAnalysis(User user, String originalFilename, String targetRole,
                          String jobDescription, String extractedText,
                          Integer overallScore, String qualityAssessment, String analysisJson) {
        this.user = user;
        this.originalFilename = originalFilename;
        this.targetRole = targetRole;
        this.jobDescription = jobDescription;
        this.extractedText = extractedText;
        this.overallScore = overallScore;
        this.qualityAssessment = qualityAssessment;
        this.analysisJson = analysisJson;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
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
