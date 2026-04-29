package com.shop.shop.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${app.base-url:https://spring-shop-backend-production.up.railway.app}")
    private String baseUrl;

    @Value("${app.upload-dir:upload/}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                log.warn("Image not found: {}", filename);
                return ResponseEntity.notFound().build();
            }

            // Determine content type
            String contentType = URLConnection.guessContentTypeFromName(filename);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error serving image: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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

            // Validate file size
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body(Map.of("error", "File size exceeds 5MB limit"));
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

            // Save file
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Generate full URL using configured base URL
            String fullUrl = baseUrl + "/upload/" + fileName;

            log.info("File uploaded successfully: {} -> {}", fileName, fullUrl);

            return ResponseEntity.ok(Map.of(
                    "fileName", fileName,
                    "url", fullUrl,
                    "fullUrl", fullUrl,
                    "fileSize", file.getSize(),
                    "contentType", contentType,
                    "originalName", originalFilename
            ));

        } catch (Exception e) {
            log.error("Upload failed", e);
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
                    if (file.isEmpty()) {
                        errors.add(file.getOriginalFilename() + " is empty");
                        continue;
                    }

                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        errors.add(file.getOriginalFilename() + " is not an image");
                        continue;
                    }

                    if (file.getSize() > MAX_FILE_SIZE) {
                        errors.add(file.getOriginalFilename() + " exceeds 5MB limit");
                        continue;
                    }

                    String originalFilename = file.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

                    Path uploadPath = Paths.get(uploadDir);
                    Files.createDirectories(uploadPath);
                    Path filePath = uploadPath.resolve(fileName);
                    Files.write(filePath, file.getBytes());

                    String fullUrl = baseUrl + "/upload/" + fileName;
                    urls.add(fullUrl);

                    uploadedFiles.add(Map.of(
                            "originalName", originalFilename,
                            "fileName", fileName,
                            "url", fullUrl,
                            "fullUrl", fullUrl,
                            "size", String.valueOf(file.getSize()),
                            "contentType", contentType
                    ));

                    log.info("File uploaded successfully in bulk: {} -> {}", fileName, fullUrl);

                } catch (Exception e) {
                    errors.add("Failed to upload " + file.getOriginalFilename() + ": " + e.getMessage());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("uploadedFiles", uploadedFiles);
            response.put("urls", urls);
            response.put("fullUrls", urls);
            response.put("totalUploaded", uploadedFiles.size());
            response.put("totalFiles", files.size());
            if (!errors.isEmpty()) {
                response.put("errors", errors);
            }

            log.info("Bulk upload completed: {} of {} files uploaded successfully", uploadedFiles.size(), files.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Bulk upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<?> deleteFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir + filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", filename);
                return ResponseEntity.ok(Map.of(
                        "message", "File deleted successfully",
                        "fileName", filename
                ));
            } else {
                log.warn("File not found for deletion: {}", filename);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Delete failed for file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Delete failed: " + e.getMessage()));
        }
    }
}