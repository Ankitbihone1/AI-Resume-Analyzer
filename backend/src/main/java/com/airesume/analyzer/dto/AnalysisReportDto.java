package com.airesume.analyzer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * AnalysisReportDto
 * Data contract representing structured AI evaluation results.
 */
public class AnalysisReportDto {

    private Integer overallScore;
    private String qualityAssessment;
    private String matchSummary;
    private List<String> matchingSkills = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();
    private List<String> keywordsFound = new ArrayList<>();
    private List<String> keywordsMissing = new ArrayList<>();
    private List<String> strengths = new ArrayList<>();
    private List<String> improvements = new ArrayList<>();
    private List<BulletPointSuggestion> bulletPointSuggestions = new ArrayList<>();

    public AnalysisReportDto() {
    }

    public static class BulletPointSuggestion {
        private String original;
        private String suggested;

        public BulletPointSuggestion() {
        }

        public BulletPointSuggestion(String original, String suggested) {
            this.original = original;
            this.suggested = suggested;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getSuggested() {
            return suggested;
        }

        public void setSuggested(String suggested) {
            this.suggested = suggested;
        }
    }

    // Getters and Setters
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

    public String getMatchSummary() {
        return matchSummary;
    }

    public void setMatchSummary(String matchSummary) {
        this.matchSummary = matchSummary;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getKeywordsFound() {
        return keywordsFound;
    }

    public void setKeywordsFound(List<String> keywordsFound) {
        this.keywordsFound = keywordsFound;
    }

    public List<String> getKeywordsMissing() {
        return keywordsMissing;
    }

    public void setKeywordsMissing(List<String> keywordsMissing) {
        this.keywordsMissing = keywordsMissing;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getImprovements() {
        return improvements;
    }

    public void setImprovements(List<String> improvements) {
        this.improvements = improvements;
    }

    public List<BulletPointSuggestion> getBulletPointSuggestions() {
        return bulletPointSuggestions;
    }

    public void setBulletPointSuggestions(List<BulletPointSuggestion> bulletPointSuggestions) {
        this.bulletPointSuggestions = bulletPointSuggestions;
    }
}
