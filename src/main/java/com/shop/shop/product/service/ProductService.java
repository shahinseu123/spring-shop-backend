package com.shop.shop.product.service;

import com.shop.shop.product.dto.ProductCreateDto;
import com.shop.shop.product.entity.Product;

public interface ProductService {
    Product create(ProductCreateDto dto);
}
