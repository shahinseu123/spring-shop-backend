package com.shop.shop.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> items;
    private Integer subtotal;
    private String appliedCouponCode;
    private Integer discountAmount;
    private Integer finalAmount;

}
