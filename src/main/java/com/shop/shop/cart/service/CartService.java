package com.shop.shop.cart.service;

import com.shop.shop.cart.dto.CartResponseDto;
import com.shop.shop.cart.entity.Cart;
import com.shop.shop.coupon.entity.Coupon;

public interface CartService {
    Cart getOrCreateCart(Long userId, String sessionId);
    CartResponseDto addItemToCart(Long cartId, Long productId, Integer quantity);
    CartResponseDto removeItemFromCart(Long cartId, Long itemId);
    CartResponseDto updateItemQuantity(Long cartId, Long itemId, Integer quantity);
    CartResponseDto getCart(Long cartId);
    void clearCart(Long cartId);
    void applyCouponToCart(Long cartId, Coupon coupon, Integer discountAmount, Integer finalAmount);
    void removeCouponFromCart(Long cartId);
    Integer calculateCartTotal(Long cartId);
}
