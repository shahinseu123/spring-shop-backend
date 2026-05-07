package com.shop.shop.category.controller;

import com.shop.shop.category.dto.CategoryDetailsDto;
import com.shop.shop.category.dto.CategoryDto;
import com.shop.shop.category.dto.CategoryTreeDTO;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import com.shop.shop.category.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryRepository.CategoryProjection>> categoryPaginated(Pageable pageable, @RequestParam("search") Optional<String> query) {
        return new ResponseEntity<>(categoryService.categoryPaginated(pageable, query.orElse(null)), HttpStatus.OK);
    }
    @GetMapping("/list")
    public ResponseEntity<List<CategoryTreeDTO>> categoryList(
            @RequestParam("search") Optional<String> query) {
        List<CategoryTreeDTO> categories = categoryService.categoryList(query.orElse(null));
        return ResponseEntity.ok(categories);
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
