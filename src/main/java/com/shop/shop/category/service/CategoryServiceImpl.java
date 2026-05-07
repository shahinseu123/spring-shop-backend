package com.shop.shop.category.service;

import com.shop.shop.category.dto.CategoryDetailsDto;
import com.shop.shop.category.dto.CategoryDto;
import com.shop.shop.category.dto.CategoryTreeDTO;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public List<CategoryTreeDTO> categoryList(String query ) {
        List<CategoryRepository.CategoryProjection> allCategories = categoryRepository.findAllCategories(query);

        if (!allCategories.isEmpty()) {
            return buildCategoryTree(allCategories);
        }
        return Collections.emptyList();
        // Build tree structure
    }

    private List<CategoryTreeDTO> buildCategoryTree(List<CategoryRepository.CategoryProjection> categories) {
        // Create map of all categories
        Map<Long, CategoryTreeDTO> categoryMap = new HashMap<>();
        List<CategoryTreeDTO> rootCategories = new ArrayList<>();

        // First pass: Create DTO objects for all categories
        for (CategoryRepository.CategoryProjection cat : categories) {
            CategoryTreeDTO dto = CategoryTreeDTO.builder()
                    .id(cat.getId())
                    .name(cat.getName())
                    .slug(cat.getSlug())
                    .imageUrl(cat.getImageUrl())
                    .createdAt(cat.getCreatedAt())
                    .subCategories(new ArrayList<>())
                    .build();
            categoryMap.put(cat.getId(), dto);
        }

        // Second pass: Build parent-child relationships
        for (CategoryRepository.CategoryProjection cat : categories) {
            CategoryTreeDTO dto = categoryMap.get(cat.getId());
            if (cat.getParentId() == null) {
                // This is a root category
                rootCategories.add(dto);
            } else {
                // This is a subcategory - add to parent
                CategoryTreeDTO parent = categoryMap.get(cat.getParentId());
                if (parent != null) {
                    parent.getSubCategories().add(dto);
                }
            }
        }

        return rootCategories;
    }

    @Override
    public Page<CategoryRepository.CategoryProjection> categoryPaginated(Pageable pageable, String query) {
        return categoryRepository.findPaginatedCategories(query, pageable);
    }
}
