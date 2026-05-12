package com.shop.shop.order.service;

import com.shop.shop.order.dto.OrderCreateDto;

public interface OrderService {
    void createOrder(OrderCreateDto dto);
}
