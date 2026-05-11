package com.shop.shop.coupon.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponResponseDto {
    private Long id;
    private String code;
    private String description;
    private String discountType;
    private Integer discountValue;
    private Integer minimumOrderAmount;
    private Integer maximumDiscountAmount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private Integer usageLimit;
    private Integer usageCount;
    private Integer perUserLimit;
    private String status;
    private List<Long> applicableProductIds;  // List of product IDs
    private List<Long> applicableCategoryIds; // List o
    private LocalDateTime createdAt;
}

