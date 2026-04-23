package com.shop.shop.category.service;

import com.shop.shop.category.dto.CategoryDetailsDto;
import com.shop.shop.category.dto.CategoryDto;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public Void create(CategoryDto dto) {
        Category newCategory = new Category();
        newCategory.setName(dto.getName());
        newCategory.setSlug(dto.getName().toLowerCase());
        newCategory.setImageUrl(dto.getImageUrl());
        if(dto.getParentId() != null) {
            categoryRepository.findById(dto.getParentId()).ifPresent(newCategory::setParent);
        }
        categoryRepository.save(newCategory);
        return null;
    }

    @Override
    public Void deletecategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDetailsDto categoryDetails(Long id) {
        CategoryRepository.CategoryProjection category = categoryRepository.findCategoryDetails(id);
        if(category == null) {
            throw new RuntimeException("Category not found");
        }
        // 2. Get subcategories
        List<CategoryRepository.CategoryProjection> subCategories = categoryRepository.findSubCategoriesByParentId(id);

        // 3. Get products
        List<CategoryRepository.ProductProjection> products = categoryRepository.findProductsByCategoryId(id);
        try {
            return new CategoryDetailsDto(
                    category.getId(),
                    category.getName(),
                    category.getSlug(),
                    category.getImageUrl(),
//                    category.getCreatedAt(),
                    subCategories,
                    products
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch category details", e);
        }
    }

    @Override
    public List<CategoryRepository.CategoryProjection> categoryList(String query) {
        return categoryRepository.findAllCategories(query);
    }
}
