package com.airesume.analyzer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * HealthController
 * Exposes a public heartbeat endpoint for container health probes and uptime checks.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ai-resume-analyzer-backend",
                "framework", "Spring Boot 3",
                "timestamp", LocalDateTime.now()
        ));
    }
}
