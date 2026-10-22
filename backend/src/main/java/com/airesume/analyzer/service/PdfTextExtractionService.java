package com.airesume.analyzer.service;

import com.airesume.analyzer.exception.InvalidFileException;
import com.airesume.analyzer.util.FileValidationUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * PdfTextExtractionService
 * Extracts and sanitizes readable text from uploaded PDF resumes using Apache PDFBox.
 * Identifies scanned/image-only PDFs and password-protected files.
 */
@Service
public class PdfTextExtractionService {

    private static final Logger logger = LoggerFactory.getLogger(PdfTextExtractionService.class);
    private static final int MIN_READABLE_TEXT_LENGTH = 50;

    /**
     * Extract clean readable text from uploaded MultipartFile resume.
     */
    public String extractText(MultipartFile file) {
        FileValidationUtil.validatePdfFile(file);

        try (InputStream inputStream = file.getInputStream()) {
            return extractTextFromStream(inputStream, file.getOriginalFilename());
        } catch (IOException ex) {
            logger.error("Failed to read uploaded resume file stream: {}", ex.getMessage());
            throw new InvalidFileException("Could not read uploaded PDF file. Please ensure the file is not corrupted.");
        }
    }

    /**
     * Extract text from an InputStream using Apache PDFBox.
     */
    public String extractTextFromStream(InputStream inputStream, String filename) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            // Check for encrypted/password-protected PDF
            if (document.isEncrypted()) {
                throw new InvalidFileException("The uploaded PDF is password-protected or encrypted. Please upload an unlocked PDF resume.");
            }

            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setSortByPosition(true);

            String rawText = textStripper.getText(document);
            String cleanedText = cleanExtractedText(rawText);

            // Scanned PDF detection: if text is empty or fewer than 50 characters, it's likely an image scan
            if (cleanedText.length() < MIN_READABLE_TEXT_LENGTH) {
                logger.warn("PDF '{}' extracted only {} characters. Flagging as scanned/unreadable.", filename, cleanedText.length());
                throw new InvalidFileException(
                        "The uploaded PDF appears to be a scanned document or contains only images without readable text. " +
                        "Please export your resume directly as a text-based PDF from Google Docs, MS Word, or Canva."
                );
            }

            logger.info("Successfully extracted {} characters from PDF resume '{}'.", cleanedText.length(), filename);
            return cleanedText;

        } catch (InvalidFileException ex) {
            throw ex;
        } catch (IOException ex) {
            logger.error("Error parsing PDF document '{}': {}", filename, ex.getMessage());
            throw new InvalidFileException("The uploaded file could not be parsed as a valid PDF. It may be damaged or incomplete.");
        }
    }

    /**
     * Normalize whitespace and remove control characters from extracted text.
     */
    public String cleanExtractedText(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("\u0000", "") // Remove null characters
                .replaceAll("\r\n", "\n")
                .replaceAll("\r", "\n")
                .replaceAll("[ \t]+", " ") // Collapse multiple spaces/tabs
                .replaceAll("\n{3,}", "\n\n") // Collapse excessive blank lines
                .trim();
    }
}
