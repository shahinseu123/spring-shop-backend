package com.shop.shop.category.controller;

import com.shop.shop.category.dto.CategoryDetailsDto;
import com.shop.shop.category.dto.CategoryDto;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import com.shop.shop.category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> create(CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryRepository.CategoryProjection>> categoryList(@RequestParam("query") Optional<String> query) {
        return new ResponseEntity<>(categoryService.categoryList(query.orElse(null)), HttpStatus.OK);
    }

    @GetMapping("/details")
    public ResponseEntity<CategoryDetailsDto> categoryDetails(@RequestParam("id") Long id) {
        return new ResponseEntity<>(categoryService.categoryDetails(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        return new ResponseEntity<>(categoryService.deletecategory(id), HttpStatus.OK);
    }

}
