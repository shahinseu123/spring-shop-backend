package com.shop.shop.order.service;

import com.shop.shop.order.dto.OrderCreateDto;
import com.shop.shop.order.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderCreateDto dto);
    OrderResponseDto getOrderById(Long orderId);
    List<OrderResponseDto> getUserOrders(Long userId);
    void cancelOrder(Long orderId);
}
