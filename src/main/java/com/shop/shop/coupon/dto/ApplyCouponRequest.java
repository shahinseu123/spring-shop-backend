package com.shop.shop.coupon.dto;

import lombok.Data;

@Data
public class ApplyCouponRequest {
        private String couponCode;
        private Long cartId;

}
