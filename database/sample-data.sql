-- ========================================================================
-- AI Resume Analyzer - Sample Development Seed Data
-- ========================================================================

USE resume_analyzer_db;

-- Insert demo user (Password: Password123! hashed with BCrypt)
-- Hash generated using standard BCrypt strength 10 for 'Password123!'
INSERT INTO users (email, password_hash, full_name, target_role, role)
VALUES (
    'demo.developer@example.com',
    '$2a$10$eACCYoNOHEqgkDw8v5UUpOF7np8DGoh58W.7xV8k0h0k88H32sB6.',
    'Alex Morgan',
    'Java Full Stack Developer',
    'ROLE_USER'
) ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- Insert sample resume analysis record
INSERT INTO resume_analyses (
    user_id,
    original_filename,
    target_role,
    job_description,
    extracted_text,
    overall_score,
    quality_assessment,
    analysis_json
)
SELECT 
    id,
    'Alex_Morgan_Resume_2026.pdf',
    'Junior Java Full Stack Developer',
    'We are seeking an enthusiastic Junior Java Full Stack Developer proficient in Java 17, Spring Boot, React.js, RESTful APIs, MySQL, and Docker. Experience with automated testing and Git is required.',
    'Alex Morgan | alex.morgan@example.com | Portfolio: alexmorgan.dev\nSKILLS:\n- Languages: Java, JavaScript, SQL, HTML5, CSS3\n- Frameworks: Spring Boot, Spring Data JPA, React.js, Express\n- Tools: Git, Maven, Docker, Postman, MySQL\nEXPERIENCE:\nSoftware Intern | TechSolutions (Jan 2025 - Present)\n- Developed 8+ REST APIs using Spring Boot and Spring Data JPA.\n- Integrated React frontend with secure JWT token authentication.\nPROJECTS:\n- E-Commerce Microservices: Spring Boot, Kafka, MySQL\n- Task Tracker Web App: React, Tailwind CSS, Spring Boot',
    82,
    'Solid technical foundation with prominent Spring Boot and React coverage. Resume can be further enhanced by incorporating quantified metrics in bullet points and demonstrating CI/CD experience.',
    JSON_OBJECT(
        'overallScore', 82,
        'qualityAssessment', 'Solid technical foundation with prominent Spring Boot and React coverage. Resume can be further enhanced by incorporating quantified metrics in bullet points.',
        'matchSummary', 'Candidate matches 82% of core technical competencies for the Junior Java Full Stack Developer position.',
        'matchingSkills', JSON_ARRAY('Java 17', 'Spring Boot', 'React.js', 'RESTful APIs', 'MySQL', 'Docker', 'Git'),
        'missingSkills', JSON_ARRAY('Automated Testing (JUnit/Mockito)', 'CI/CD Pipelines (GitHub Actions/Jenkins)'),
        'keywordsFound', JSON_ARRAY('Spring Boot', 'React', 'REST', 'JPA', 'JWT', 'Docker', 'Maven', 'SQL'),
        'keywordsMissing', JSON_ARRAY('JUnit 5', 'Mockito', 'Agile/Scrum', 'CI/CD'),
        'strengths', JSON_ARRAY(
            'Strong alignment with both Java backend (Spring Boot) and React frontend technologies',
            'Demonstrated hands-on experience building REST APIs with JWT security',
            'Clear project portfolio and GitHub presence'
        ),
        'improvements', JSON_ARRAY(
            'Add measurable outcomes and metrics to experience bullet points (e.g., latency reduction, user count)',
            'Highlight automated testing experience (JUnit 5, Mockito, MockMvc)',
            'Mention familiarity with CI/CD tools such as GitHub Actions'
        ),
        'bulletPointSuggestions', JSON_ARRAY(
            JSON_OBJECT(
                'original', 'Developed 8+ REST APIs using Spring Boot and Spring Data JPA.',
                'suggested', 'Designed and implemented 8+ robust RESTful endpoints with Spring Boot and Spring Data JPA, cutting query execution latency by 25% through optimized indexing.'
            )
        )
    )
FROM users 
WHERE email = 'demo.developer@example.com'
LIMIT 1;
