package com.airesume.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AI Resume Analyzer Application
 * Entry point for the Spring Boot REST API service.
 */
@SpringBootApplication
public class AiResumeAnalyzerApplication {

    private static final Logger logger = LoggerFactory.getLogger(AiResumeAnalyzerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AiResumeAnalyzerApplication.class, args);
        logger.info("=================================================================");
        logger.info("🚀 AI Resume Analyzer Backend Service is running!");
        logger.info("📡 API Base URL: http://localhost:8080/api");
        logger.info("=================================================================");
    }
}
