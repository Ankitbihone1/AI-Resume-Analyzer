package com.airesume.analyzer.dto;

import com.airesume.analyzer.entity.User;
import java.time.LocalDateTime;

/**
 * UserDto
 * Safe representation of a user without sensitive credentials.
 */
public class UserDto {

    private Long id;
    private String email;
    private String fullName;
    private String targetRole;
    private String role;
    private LocalDateTime createdAt;

    public UserDto() {
    }

    public UserDto(Long id, String email, String fullName, String targetRole, String role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.targetRole = targetRole;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserDto fromEntity(User user) {
        if (user == null) return null;
        return new UserDto(
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getTargetRole(),
            user.getRole(),
            user.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
