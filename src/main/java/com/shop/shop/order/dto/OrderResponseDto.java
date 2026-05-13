package com.shop.shop.order.dto;


import com.shop.shop.order.entity.OrderStatus;
import com.shop.shop.order.entity.PaymentMethod;
import com.shop.shop.order.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;

    // Contact
    private String email;
    private String phone;
    private String firstName;
    private String lastName;

    // Shipping
    private ShippingAddressDto shippingAddress;

    // Financial
    private Integer subtotal;
    private Integer shippingCost;
    private Integer discountAmount;
    private Integer couponDiscount;
    private Integer totalAmount;

    // Status
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;

    // Coupon
    private String couponCode;

    // Items
    private List<OrderItemDto> items;

    // Notes
    private String notes;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
}
