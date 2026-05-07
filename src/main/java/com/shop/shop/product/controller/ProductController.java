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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ProductCreateDto dto) {
        return new ResponseEntity<>(productService.create(dto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ProductRepository.ProductProjection>> paginatedProducts(
            @RequestParam("search") Optional<String> query,
            @RequestParam("categoryName") Optional<String> categoryName,
            @RequestParam("brandId") Optional<List<Long>> brandIds,  // Changed to List<Long>
            Pageable pageable) {

        return new ResponseEntity<>(
                productService.paginatedProducts(
                        pageable,
                        query.orElse(null),
                        categoryName.orElse(null),
                        brandIds.orElse(Collections.emptyList())  // Pass brand IDs
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> productDetails(@PathVariable Long id) {
        return new ResponseEntity<>(productService.productDetails(id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> productDelete(@PathVariable Long id) {
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }


}
