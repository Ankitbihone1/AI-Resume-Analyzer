package com.airesume.analyzer.service;

import com.airesume.analyzer.exception.InvalidFileException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PdfTextExtractionServiceTest {

    private PdfTextExtractionService extractionService;

    @BeforeEach
    void setUp() {
        extractionService = new PdfTextExtractionService();
    }

    private byte[] createPdfWithText(String content) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(content);
                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    @Test
    @DisplayName("Should successfully extract text from valid PDF resume")
    void testExtractText_Success() throws IOException {
        String sampleResumeText = "Alex Morgan - Java Full Stack Developer with experience in Spring Boot, React, and MySQL databases.";
        byte[] pdfBytes = createPdfWithText(sampleResumeText);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "Alex_Resume.pdf",
                "application/pdf",
                pdfBytes
        );

        String extracted = extractionService.extractText(file);
        assertNotNull(extracted);
        assertTrue(extracted.contains("Alex Morgan"));
        assertTrue(extracted.contains("Spring Boot"));
    }

    @Test
    @DisplayName("Should throw InvalidFileException when PDF contains fewer than 50 characters (Scanned/Empty PDF)")
    void testExtractText_ScannedOrEmpty_ThrowsException() throws IOException {
        String shortText = "Hi"; // < 50 characters
        byte[] pdfBytes = createPdfWithText(shortText);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "scanned_doc.pdf",
                "application/pdf",
                pdfBytes
        );

        InvalidFileException ex = assertThrows(InvalidFileException.class, () -> extractionService.extractText(file));
        assertTrue(ex.getMessage().contains("scanned document"));
    }

    @Test
    @DisplayName("Should throw InvalidFileException when uploaded file is empty")
    void testExtractText_EmptyFile_ThrowsException() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        assertThrows(InvalidFileException.class, () -> extractionService.extractText(emptyFile));
    }

    @Test
    @DisplayName("Should throw InvalidFileException when file extension is not .pdf")
    void testExtractText_InvalidExtension_ThrowsException() {
        MockMultipartFile nonPdf = new MockMultipartFile(
                "file",
                "resume.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "Mock document content here that has more than 50 characters to test extension check.".getBytes()
        );

        assertThrows(InvalidFileException.class, () -> extractionService.extractText(nonPdf));
    }

    @Test
    @DisplayName("Should clean whitespace and control characters properly")
    void testCleanExtractedText() {
        String dirtyText = "   John   Doe \r\n\r\n\r\n  Software   Engineer \u0000  ";
        String cleaned = extractionService.cleanExtractedText(dirtyText);

        assertEquals("John Doe \n\nSoftware Engineer", cleaned);
    }
}
