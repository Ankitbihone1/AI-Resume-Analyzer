package com.airesume.analyzer.controller;

import com.airesume.analyzer.dto.PasswordChangeRequest;
import com.airesume.analyzer.dto.UpdateProfileRequest;
import com.airesume.analyzer.dto.UserDto;
import com.airesume.analyzer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UserController
 * Manages profile retrieval, profile updates, and password changes for the authenticated user.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get current user's profile.
     * GET /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto userDto = userService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(userDto);
    }

    /**
     * Update current user's full name and target role.
     * PUT /api/users/me
     */
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserDto updatedUser = userService.updateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Change current user's password.
     * PUT /api/users/me/password
     */
    @PutMapping("/me/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
    }
}
