package com.shop.shop.coupon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppliedCouponResponseDto {
    private String couponCode;
    private String discountType;
    private Integer discountValue;
    private Integer discountAmount;
    private Integer originalAmount;
    private Integer finalAmount;
}
