package com.shop.shop.coupon.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class CouponCreateDto {
    private String code;
    private String description;
    private String discountType;
    private Integer discountValue;
    private Integer minimumOrderAmount;
    private Integer maximumDiscountAmount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private Integer usageLimit;
    private Integer perUserLimit;

    // New fields for ManyToMany relationships
    private List<Long> applicableProductIds;  // List of product IDs
    private List<Long> applicableCategoryIds; // List o
}

