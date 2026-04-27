package com.shop.shop.product.service;

import com.shop.shop.product.dto.ProductCreateDto;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product create(ProductCreateDto dto);
    Page <ProductRepository.ProductProjection> paginatedProducts(Pageable pageable, String query);
    ProductRepository.ProductDetailsProjection productDetails(Long id);
    Void deleteProduct(Long id);


}
