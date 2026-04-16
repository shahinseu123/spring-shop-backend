package com.shop.shop.category.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private String parentName;
    private String slug;
    private String imageUrl;
    private Boolean isActive;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
