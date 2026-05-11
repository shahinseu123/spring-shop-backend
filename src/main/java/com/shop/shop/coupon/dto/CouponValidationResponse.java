package com.shop.shop.coupon.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CouponValidationResponse {
        private boolean valid;
        private Integer discountAmount;
        private Integer finalAmount;
        private String message;
        private CouponResponseDto coupon;
}
