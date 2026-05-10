package com.shop.shop.coupon.dto;

import java.math.BigDecimal;

public class CouponValidationDto {
    private boolean valid;
    private String message;
    private BigDecimal discountAmount;
    private BigDecimal newTotal;
}
