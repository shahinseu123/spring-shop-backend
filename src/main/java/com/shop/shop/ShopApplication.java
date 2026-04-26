package com.shop.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

	private static void createUploadDirectory() {
		try {
			Path uploadDir = Paths.get("upload/");
			if (!Files.exists(uploadDir)) {
				Files.createDirectories(uploadDir);
				System.out.println("Upload directory created at: " + uploadDir.toAbsolutePath());
			}
		} catch (Exception e) {
			System.err.println("Failed to create upload directory: " + e.getMessage());
		}
	}

}
