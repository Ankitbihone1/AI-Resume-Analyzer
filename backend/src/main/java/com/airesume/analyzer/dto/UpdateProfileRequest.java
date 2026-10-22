package com.airesume.analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * UpdateProfileRequest DTO
 */
public class UpdateProfileRequest {

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    private String targetRole;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String fullName, String targetRole) {
        this.fullName = fullName;
        this.targetRole = targetRole;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }
}
