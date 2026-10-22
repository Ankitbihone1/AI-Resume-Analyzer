package com.airesume.analyzer.service;

import com.airesume.analyzer.dto.AuthResponse;
import com.airesume.analyzer.dto.LoginRequest;
import com.airesume.analyzer.dto.RegisterRequest;
import com.airesume.analyzer.entity.User;
import com.airesume.analyzer.exception.UserAlreadyExistsException;
import com.airesume.analyzer.repository.UserRepository;
import com.airesume.analyzer.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "Jane Doe",
                "jane.doe@example.com",
                "Java Full Stack Developer",
                "Password123!"
        );

        loginRequest = new LoginRequest(
                "jane.doe@example.com",
                "Password123!"
        );

        testUser = new User(
                "jane.doe@example.com",
                "encoded_password_hash",
                "Jane Doe",
                "Java Full Stack Developer"
        );
        testUser.setId(10L);
    }

    @Test
    @DisplayName("Should successfully register a new user and return JWT token")
    void testRegister_Success() {
        when(userRepository.existsByEmail("jane.doe@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("encoded_password_hash");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(tokenProvider.generateToken(eq("jane.doe@example.com"), eq(10L)))
                .thenReturn("mock_jwt_token_12345");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mock_jwt_token_12345", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("jane.doe@example.com", response.getUser().getEmail());
        assertEquals("Jane Doe", response.getUser().getFullName());

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when registering with duplicate email")
    void testRegister_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail("jane.doe@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully authenticate valid credentials and issue JWT token")
    void testLogin_Success() {
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(userRepository.findByEmail("jane.doe@example.com"))
                .thenReturn(Optional.of(testUser));
        when(tokenProvider.generateToken(mockAuth)).thenReturn("mock_login_jwt_token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock_login_jwt_token", response.getToken());
        assertEquals("jane.doe@example.com", response.getUser().getEmail());
    }

    @Test
    @DisplayName("Should propagate BadCredentialsException when credentials are invalid")
    void testLogin_InvalidCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }
}
