package com.shop.shop.category.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shop.shop.category.entity.Category;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;
    private String slug;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
