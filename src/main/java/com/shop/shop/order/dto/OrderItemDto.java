package com.shop.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;
    private Integer discountPrice;
}
