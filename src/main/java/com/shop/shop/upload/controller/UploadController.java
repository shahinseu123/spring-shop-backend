package com.shop.shop.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/uploads")
public class UploadController {
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            // Validate image type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed"));
            }

            // Validate file size (5MB max)
            long maxSize = 5 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body(Map.of("error", "File size exceeds 5MB limit"));
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + extension;

            // Save file to /upload directory
            Path uploadDir = Paths.get("upload/");
            Files.createDirectories(uploadDir);
            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Return path starting from /upload
            String fileUrl = "/upload/" + fileName;

            return ResponseEntity.ok(Map.of(
                    "fileName", fileName,
                    "url", fileUrl,
                    "fileSize", file.getSize(),
                    "contentType", contentType
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> uploadMultiple(@RequestParam("files") List<MultipartFile> files) {
        try {
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "No files provided"));
            }

            List<Map<String, String>> uploadedFiles = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            for (MultipartFile file : files) {
                try {
                    // Validate file
                    if (file.isEmpty()) {
                        errors.add(file.getOriginalFilename() + " is empty");
                        continue;
                    }

                    // Validate image type
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        errors.add(file.getOriginalFilename() + " is not an image");
                        continue;
                    }

                    // Validate file size
                    if (file.getSize() > 5 * 1024 * 1024) {
                        errors.add(file.getOriginalFilename() + " exceeds 5MB limit");
                        continue;
                    }

                    // Generate unique filename
                    String originalFilename = file.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

                    // Save file to /upload directory
                    Path uploadDir = Paths.get("upload/");
                    Files.createDirectories(uploadDir);
                    Path filePath = uploadDir.resolve(fileName);
                    Files.write(filePath, file.getBytes());

                    // Create URL
                    String fileUrl = "/upload/" + fileName;
                    urls.add(fileUrl);

                    uploadedFiles.add(Map.of(
                            "originalName", originalFilename,
                            "fileName", fileName,
                            "url", fileUrl,
                            "size", String.valueOf(file.getSize())
                    ));

                } catch (Exception e) {
                    errors.add("Failed to upload " + file.getOriginalFilename() + ": " + e.getMessage());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("uploadedFiles", uploadedFiles);
            response.put("urls", urls);
            response.put("totalUploaded", uploadedFiles.size());
            response.put("totalFiles", files.size());
            if (!errors.isEmpty()) {
                response.put("errors", errors);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }
}
