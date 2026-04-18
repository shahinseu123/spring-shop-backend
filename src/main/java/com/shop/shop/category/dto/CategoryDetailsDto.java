package com.shop.shop.category.dto;

import com.shop.shop.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CategoryDetailsDto {
    private Long id;
    private String name;
    private String slug;
    private String imageUrl;
//    private LocalDateTime createdAt;
    private List<CategoryRepository.CategoryProjection> subCategories;
    private List<CategoryRepository.ProductProjection> products;
}
