package com.shop.shop.category.service;

import com.shop.shop.category.dto.CategoryDetailsDto;
import com.shop.shop.category.dto.CategoryDto;
import com.shop.shop.category.dto.CategoryTreeDTO;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Void create(CategoryDto dto);
    Void deletecategory(Long id);
    CategoryDetailsDto categoryDetails(Long id);
    List<CategoryTreeDTO> categoryList(String query);
    Page<CategoryRepository.CategoryProjection> categoryPaginated(Pageable pageable, String query);
}
