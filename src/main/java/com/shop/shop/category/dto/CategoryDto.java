package com.shop.shop.category.dto;

import com.shop.shop.category.entity.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;
    private String slug;
    private String imageUrl;
    private LocalDateTime createdAt;
}
