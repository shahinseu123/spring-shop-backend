package com.shop.shop.category.service;

import com.shop.shop.category.dto.CategoryDetailsDto;
import com.shop.shop.category.dto.CategoryDto;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;

import java.util.List;

public interface CategoryService {
    Void create(CategoryDto dto);
    Void deletecategory(Long id);
    CategoryDetailsDto categoryDetails(Long id);
    List<CategoryRepository.CategoryProjection> categoryList(String query);
}
