package com.shop.shop.coupon.service;

import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import com.shop.shop.coupon.dto.CouponCreateDto;
import com.shop.shop.coupon.entity.Coupon;
import com.shop.shop.coupon.entity.CouponStatus;
import com.shop.shop.coupon.entity.DiscountType;
import com.shop.shop.coupon.repository.CouponRepository;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CouponServiceImpl(CouponRepository couponRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.couponRepository = couponRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public Void create(CouponCreateDto dto) {
        DiscountType discountType = DiscountType.valueOf(dto.getDiscountType());

        if (couponRepository.existsCouponByCode(dto.getCode().toUpperCase())) {
            throw new RuntimeException("Coupon code already exists: " + dto.getCode());
        }

        if (dto.getValidFrom().isAfter(dto.getValidUntil())) {
            throw new RuntimeException("Valid from date cannot be after valid until date");
        }

        if (dto.getDiscountValue() <= 0) {
            throw new RuntimeException("Discount value must be greater than 0");
        }

        if (discountType == DiscountType.PERCENTAGE) {
            if (dto.getDiscountValue() > 100) {
                throw new RuntimeException("Percentage discount must be between 1 and 100");
            }
        }

        if (dto.getMinimumOrderAmount() < 0) {
            throw new RuntimeException("Minimum order amount cannot be negative");
        }

        if (dto.getUsageLimit() != null && dto.getUsageLimit() < 1) {
            throw new RuntimeException("Usage limit must be at least 1");
        }

        if (dto.getPerUserLimit() != null && dto.getPerUserLimit() < 1) {
            throw new RuntimeException("Per user limit must be at least 1");
        }

        // 6. Create coupon with uppercase code
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(dto.getCode().toUpperCase()); // Store in uppercase
        newCoupon.setDescription(dto.getDescription());
        newCoupon.setDiscountType(DiscountType.valueOf(dto.getDiscountType()));
        newCoupon.setDiscountValue(dto.getDiscountValue());
        newCoupon.setMaximumDiscountAmount(dto.getMaximumDiscountAmount());
        newCoupon.setMinimumOrderAmount(dto.getMinimumOrderAmount());
        newCoupon.setValidFrom(dto.getValidFrom());
        newCoupon.setValidUntil(dto.getValidUntil());
        newCoupon.setUsageLimit(dto.getUsageLimit());
        newCoupon.setPerUserLimit(dto.getPerUserLimit());
        newCoupon.setUsageCount(0); // Initialize usage count
        newCoupon.setStatus(CouponStatus.ACTIVE); // Set default status
        // 9. Handle ManyToMany - Applicable Products
        if (dto.getApplicableProductIds() != null && !dto.getApplicableProductIds().isEmpty()) {
            Set<Product> applicableProducts = new HashSet<>();
            for (Long productId : dto.getApplicableProductIds()) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
                applicableProducts.add(product);
            }
            newCoupon.setApplicableProducts(applicableProducts);
        }

        // 10. Handle ManyToMany - Applicable Categories
        if (dto.getApplicableCategoryIds() != null && !dto.getApplicableCategoryIds().isEmpty()) {
            Set<Category> applicableCategories = new HashSet<>();
            for (Long categoryId : dto.getApplicableCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
                applicableCategories.add(category);
            }
            newCoupon.setApplicableCategories(applicableCategories);
        }

         couponRepository.save(newCoupon);
        return null;
    }
}
