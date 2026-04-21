package com.shop.shop.product.controller;

import com.shop.shop.product.dto.ProductCreateDto;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.repository.ProductRepository;
import com.shop.shop.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductCreateDto dto) {
        return new ResponseEntity<>(productService.create(dto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ProductRepository.ProductProjection>> paginatedProducts(@RequestParam("query") Optional<String> query, Pageable pageable) {
        return new ResponseEntity<>(productService.paginatedProducts(pageable, query.orElse(null)), HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> productDetails(@PathVariable Long id) {
        return new ResponseEntity<>(productService.productDetails(id), HttpStatus.OK);
    }


}
