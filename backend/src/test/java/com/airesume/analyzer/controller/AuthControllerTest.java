package com.airesume.analyzer.controller;

import com.airesume.analyzer.dto.AuthResponse;
import com.airesume.analyzer.dto.LoginRequest;
import com.airesume.analyzer.dto.RegisterRequest;
import com.airesume.analyzer.dto.UserDto;
import com.airesume.analyzer.security.CustomUserDetailsService;
import com.airesume.analyzer.security.JwtTokenProvider;
import com.airesume.analyzer.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("POST /api/auth/register - Should return 201 Created on valid input")
    void testRegister_Success() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "Jane Doe",
                "jane.doe@example.com",
                "Java Full Stack Developer",
                "Password123!"
        );

        UserDto userDto = new UserDto(1L, "jane.doe@example.com", "Jane Doe", "Java Full Stack Developer", "ROLE_USER", LocalDateTime.now());
        AuthResponse response = new AuthResponse("mock_token_jwt", userDto);

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mock_token_jwt"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("jane.doe@example.com"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should return 400 Bad Request on invalid email and short password")
    void testRegister_ValidationFailure() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest(
                "",
                "invalid-email",
                "",
                "short"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.password").exists());
    }

    @Test
    @DisplayName("POST /api/auth/login - Should return 200 OK on valid credentials")
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("jane.doe@example.com", "Password123!");
        UserDto userDto = new UserDto(1L, "jane.doe@example.com", "Jane Doe", "Java Full Stack Developer", "ROLE_USER", LocalDateTime.now());
        AuthResponse response = new AuthResponse("mock_login_jwt", userDto);

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock_login_jwt"))
                .andExpect(jsonPath("$.user.email").value("jane.doe@example.com"));
    }
}
