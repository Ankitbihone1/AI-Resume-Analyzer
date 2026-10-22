package com.airesume.analyzer.service;

import com.airesume.analyzer.dto.PasswordChangeRequest;
import com.airesume.analyzer.dto.UpdateProfileRequest;
import com.airesume.analyzer.dto.UserDto;
import com.airesume.analyzer.entity.User;
import com.airesume.analyzer.exception.ResourceNotFoundException;
import com.airesume.analyzer.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService
 * Manages candidate profile updates, user lookups, and credential changes.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get user profile details by email.
     */
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(String email) {
        User user = getUserByEmail(email);
        return UserDto.fromEntity(user);
    }

    /**
     * Update editable profile fields (full name, target role).
     */
    @Transactional
    public UserDto updateProfile(String email, UpdateProfileRequest request) {
        User user = getUserByEmail(email);

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }
        if (request.getTargetRole() != null) {
            user.setTargetRole(request.getTargetRole().trim());
        }

        User updatedUser = userRepository.save(user);
        return UserDto.fromEntity(updatedUser);
    }

    /**
     * Change user password securely.
     */
    @Transactional
    public void changePassword(String email, PasswordChangeRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Current password does not match records.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Internal helper to load User entity.
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
