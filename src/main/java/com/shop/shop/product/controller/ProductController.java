package com.shop.shop.product.controller;

import com.shop.shop.product.dto.ProductCreateDto;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductCreateDto dto) {
        System.out.println(dto);
        return new ResponseEntity<>(productService.create(dto), HttpStatus.OK);
    }

}
