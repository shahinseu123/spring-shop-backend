package com.shop.shop.cart.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Long categoryId;
    private String categoryName;
    private String selectedSize;
    private String selectedColor;

    public CartItemDto() {}

    public CartItemDto(Long productId, Integer quantity, BigDecimal unitPrice, Long categoryId) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.categoryId = categoryId;
    }
}
