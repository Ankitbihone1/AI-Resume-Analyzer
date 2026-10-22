package com.airesume.analyzer.controller;

import com.airesume.analyzer.dto.ResumeAnalysisResponseDto;
import com.airesume.analyzer.entity.ResumeAnalysis;
import com.airesume.analyzer.entity.User;
import com.airesume.analyzer.security.CustomUserDetailsService;
import com.airesume.analyzer.security.JwtTokenProvider;
import com.airesume.analyzer.service.ResumeAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResumeAnalysisController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResumeAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumeAnalysisService resumeAnalysisService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;
    private ResumeAnalysis testAnalysis;

    @BeforeEach
    void setUp() {
        testUser = new User("candidate@example.com", "hash", "Alex Morgan", "Java Developer");
        testUser.setId(1L);

        testAnalysis = new ResumeAnalysis(
                testUser,
                "Alex_Resume.pdf",
                "Junior Java Full Stack Developer",
                "Job requirements in Spring Boot, React, and MySQL.",
                "Resume content here with skills and projects.",
                85,
                "Strong candidate match.",
                "{\"overallScore\":85,\"matchingSkills\":[\"Java\",\"Spring Boot\"]}"
        );
        testAnalysis.setId(100L);
    }

    @Test
    @WithMockUser(username = "candidate@example.com")
    @DisplayName("POST /api/analyses - Should successfully evaluate resume upload and return 201 Created")
    void testAnalyzeResume_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "Alex_Resume.pdf",
                "application/pdf",
                "Valid PDF sample content exceeding 50 characters to pass validation checks.".getBytes()
        );

        when(resumeAnalysisService.analyzeAndSave(any(), eq("Junior Java Full Stack Developer"), any(), eq("candidate@example.com")))
                .thenReturn(testAnalysis);

        mockMvc.perform(multipart("/api/analyses")
                        .file(file)
                        .param("targetRole", "Junior Java Full Stack Developer")
                        .param("jobDescription", "Job requirements in Spring Boot, React, and MySQL."))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.overallScore").value(85))
                .andExpect(jsonPath("$.targetRole").value("Junior Java Full Stack Developer"));
    }

    @Test
    @WithMockUser(username = "candidate@example.com")
    @DisplayName("GET /api/analyses - Should return list of user's evaluations")
    void testGetUserAnalyses_Success() throws Exception {
        when(resumeAnalysisService.getUserAnalyses("candidate@example.com"))
                .thenReturn(List.of(testAnalysis));

        mockMvc.perform(get("/api/analyses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].overallScore").value(85));
    }

    @Test
    @WithMockUser(username = "candidate@example.com")
    @DisplayName("GET /api/analyses/{id} - Should return specific evaluation report")
    void testGetAnalysisById_Success() throws Exception {
        when(resumeAnalysisService.getAnalysisByIdAndUser(100L, "candidate@example.com"))
                .thenReturn(testAnalysis);

        mockMvc.perform(get("/api/analyses/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.overallScore").value(85));
    }

    @Test
    @WithMockUser(username = "candidate@example.com")
    @DisplayName("DELETE /api/analyses/{id} - Should successfully delete report")
    void testDeleteAnalysis_Success() throws Exception {
        doNothing().when(resumeAnalysisService).deleteAnalysis(100L, "candidate@example.com");

        mockMvc.perform(delete("/api/analyses/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Resume analysis report deleted successfully."));
    }
}
