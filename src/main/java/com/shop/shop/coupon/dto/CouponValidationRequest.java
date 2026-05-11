package com.shop.shop.coupon.dto;

import lombok.Data;

import java.util.List;

@Data
public class CouponValidationRequest {
        private String couponCode;
        private Integer cartTotal;
        private Long userId;
        private List<Long> productIds;
        private List<Long> categoryIds;

}
