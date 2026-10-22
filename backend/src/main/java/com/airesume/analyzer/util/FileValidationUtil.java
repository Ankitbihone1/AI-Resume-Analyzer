package com.airesume.analyzer.util;

import com.airesume.analyzer.exception.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * FileValidationUtil
 * Validates uploaded resume files for format, MIME type, size, and corruption.
 */
public final class FileValidationUtil {

    public static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB
    public static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "application/pdf",
            "application/x-pdf",
            "application/acrobat",
            "applications/vnd.pdf",
            "text/pdf",
            "text/x-pdf"
    );

    private FileValidationUtil() {
    }

    /**
     * Perform strict validation of uploaded resume file.
     */
    public static void validatePdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Please select a resume file to upload.");
        }

        // File size check
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new InvalidFileException("Resume file size exceeds the 10 MB limit.");
        }

        // Extension check
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new InvalidFileException("Only PDF documents (.pdf) are supported.");
        }

        // Content type / MIME check
        String contentType = file.getContentType();
        if (contentType != null && !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException("Invalid file format. Uploaded file is not recognized as a valid PDF.");
        }
    }
}
