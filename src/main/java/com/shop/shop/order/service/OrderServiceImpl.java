package com.shop.shop.order.service;

import com.shop.shop.order.dto.OrderCreateDto;
import com.shop.shop.order.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService{

    @Transactional 
    @Override
    public void createOrder(OrderCreateDto dto) {
        Order newOrder = new Order();
    }
}
