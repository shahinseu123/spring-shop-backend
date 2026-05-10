package com.shop.shop.product.service;

import com.shop.shop.product.dto.ProductCreateDto;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Void create(ProductCreateDto dto);
    Page <ProductRepository.ProductProjection> paginatedProducts(Pageable pageable, String query, String categoryName, List<Long> brandIds);
    ProductRepository.ProductDetailsProjection productDetails(Long id);
    Void deleteProduct(Long id);
    List<ProductRepository.ProductProjection> productList(String query);


}
