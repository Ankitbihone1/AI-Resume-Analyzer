package com.airesume.analyzer.service;

import com.airesume.analyzer.dto.AuthResponse;
import com.airesume.analyzer.dto.LoginRequest;
import com.airesume.analyzer.dto.RegisterRequest;
import com.airesume.analyzer.dto.UserDto;
import com.airesume.analyzer.entity.User;
import com.airesume.analyzer.exception.UserAlreadyExistsException;
import com.airesume.analyzer.repository.UserRepository;
import com.airesume.analyzer.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService
 * Handles candidate registration, credential authentication, and JWT issuance.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Register a new user account.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException("An account with email " + normalizedEmail + " already exists.");
        }

        // Secure password hashing with BCrypt
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                normalizedEmail,
                hashedPassword,
                request.getFullName().trim(),
                request.getTargetRole() != null ? request.getTargetRole().trim() : "Software Engineer"
        );

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = tokenProvider.generateToken(savedUser.getEmail(), savedUser.getId());

        return new AuthResponse(token, UserDto.fromEntity(savedUser));
    }

    /**
     * Authenticate an existing user with email and password.
     */
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found after authentication"));

        String token = tokenProvider.generateToken(authentication);

        return new AuthResponse(token, UserDto.fromEntity(user));
    }
}
