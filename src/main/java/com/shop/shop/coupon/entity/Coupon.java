package com.shop.shop.coupon.entity;

import com.shop.shop.category.entity.Category;
import com.shop.shop.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // PERCENTAGE, FIXED_AMOUNT

    private Integer discountValue; // 10 for 10% or $10

    private Integer minimumOrderAmount; // Minimum cart total to apply

    private Integer maximumDiscountAmount; // Max discount for percentage coupons

    private LocalDate validFrom;

    private LocalDate validUntil;

    private Integer usageLimit; // Total times coupon can be used

    private Integer usageCount; // Current usage count

    private Integer perUserLimit; // Times single user can use

    @Enumerated(EnumType.STRING)
    private CouponStatus status; // ACTIVE, EXPIRED, DISABLED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Applicable products (if specific products)
    @ManyToMany
    @JoinTable(
            name = "coupon_products",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> applicableProducts = new HashSet<>();

    // Applicable categories
    @ManyToMany
    @JoinTable(
            name = "coupon_categories",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> applicableCategories = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        usageCount = 0;
        status = CouponStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

