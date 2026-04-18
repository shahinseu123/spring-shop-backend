package com.shop.shop.upload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/uploads")
public class UploadController {
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Generate unique filename
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = Paths.get("uploads/" + fileName);

            // Create directory if not exists
            Files.createDirectories(filePath.getParent());

            // Save file
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("Uploaded: " + fileName);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed");
        }
    }

}
