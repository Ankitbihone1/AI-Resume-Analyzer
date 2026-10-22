package com.airesume.analyzer.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * SampleResumeGenerator
 * Generates an interview-ready, non-sensitive sample resume PDF for testing.
 */
public class SampleResumeGenerator {

    @Test
    public void generateSamplePdf() throws IOException {
        File outputDir = new File("sample-resumes");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File targetFile = new File(outputDir, "Sample_Alex_Morgan_Resume.pdf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(50, 750);
                cs.showText("Alex Morgan");

                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(0, -20);
                cs.showText("alex.morgan@example.com | (555) 019-2834 | linkedin.com/in/alexmorgan-dev | github.com/alexmorgan");

                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(0, -30);
                cs.showText("PROFESSIONAL SUMMARY");

                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(0, -18);
                cs.showText("Passionate Java Full Stack Developer with hands-on experience building scalable REST APIs");
                cs.newLineAtOffset(0, -14);
                cs.showText("using Spring Boot, Spring Security, Hibernate, and modern React.js web interfaces.");

                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(0, -25);
                cs.showText("TECHNICAL SKILLS");

                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(0, -18);
                cs.showText("- Languages: Java 17, JavaScript (ES6+), SQL, HTML5, CSS3");
                cs.newLineAtOffset(0, -14);
                cs.showText("- Frameworks & Libraries: Spring Boot 3, Spring Data JPA, Spring Security, React.js, Tailwind CSS");
                cs.newLineAtOffset(0, -14);
                cs.showText("- Databases & Tools: MySQL, PostgreSQL, Docker, Git, Maven, Postman");

                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(0, -25);
                cs.showText("WORK EXPERIENCE");

                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(0, -18);
                cs.showText("Software Engineering Intern | CloudTech Solutions (Jan 2025 - Present)");

                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(0, -15);
                cs.showText("- Developed 8+ robust RESTful API endpoints with Spring Boot and Spring Data JPA.");
                cs.newLineAtOffset(0, -14);
                cs.showText("- Integrated React frontend client with JWT stateless bearer token authentication.");
                cs.newLineAtOffset(0, -14);
                cs.showText("- Optimized MySQL database schema queries, reducing endpoint response times.");

                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(0, -25);
                cs.showText("FEATURED PROJECTS");

                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(0, -18);
                cs.showText("AI Resume Analyzer Platform (Spring Boot 3, React 18, MySQL)");

                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(0, -15);
                cs.showText("- Built end-to-end resume evaluation system parsing PDF text via Apache PDFBox.");
                cs.newLineAtOffset(0, -14);
                cs.showText("- Implemented AI matching engine comparing candidate skills against job descriptions.");

                cs.endText();
            }

            document.save(targetFile);
        }

        System.out.println("Generated sample test resume at: " + targetFile.getAbsolutePath());
    }
}
