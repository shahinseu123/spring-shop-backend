package com.shop.shop.coupon.dto;

import com.shop.shop.cart.dto.CartItemDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ApplyCouponRequestDto {
    private String code;
    private BigDecimal cartTotal;
    private Long userId;
    private List<CartItemDto> cartItems;
}
