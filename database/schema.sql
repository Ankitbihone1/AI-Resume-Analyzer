-- ========================================================================
-- AI Resume Analyzer - Database Schema DDL
-- Database: MySQL 8.0+
-- Character Set: utf8mb4 / utf8mb4_unicode_ci
-- ========================================================================

CREATE DATABASE IF NOT EXISTS resume_analyzer_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE resume_analyzer_db;

-- ------------------------------------------------------------------------
-- Table: users
-- Stores registered user accounts with encrypted credentials and roles
-- ------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    target_role VARCHAR(100) NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------
-- Table: resume_analyses
-- Stores extracted resume text, job requirements, scores, and structured AI evaluation reports
-- ------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS resume_analyses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    target_role VARCHAR(150) NOT NULL,
    job_description MEDIUMTEXT NOT NULL,
    extracted_text MEDIUMTEXT NULL,
    overall_score INT NOT NULL,
    quality_assessment TEXT NULL,
    analysis_json JSON NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_analyses_user 
        FOREIGN KEY (user_id) REFERENCES users(id) 
        ON DELETE CASCADE,
    INDEX idx_analyses_user_date (user_id, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
