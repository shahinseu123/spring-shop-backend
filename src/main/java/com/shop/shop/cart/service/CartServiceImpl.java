package com.shop.shop.cart.service;

import com.shop.shop.cart.dto.CartItemDto;
import com.shop.shop.cart.dto.CartResponseDto;
import com.shop.shop.cart.entity.Cart;
import com.shop.shop.cart.entity.CartItem;
import com.shop.shop.cart.repository.CartRepository;
import com.shop.shop.cart.service.CartService;
import com.shop.shop.coupon.entity.Coupon;
import com.shop.shop.coupon.repository.CouponRepository;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    private Integer convertToCents(BigDecimal price) {
        if (price == null) return 0;
        return price.multiply(BigDecimal.valueOf(100)).intValue();
    }

    @Override
    public Cart getOrCreateCart(Long userId, String sessionId) {
        if (userId != null) {
            return cartRepository.findByUserId(userId)
                    .orElseGet(() -> createNewCart(userId, null));
        } else if (sessionId != null) {
            return cartRepository.findBySessionId(sessionId)
                    .orElseGet(() -> createNewCart(null, sessionId));
        }
        throw new RuntimeException("Either userId or sessionId must be provided");
    }

    private Cart createNewCart(Long userId, String sessionId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setSessionId(sessionId);
        cart.setSubtotal(0);
        cart.setDiscountAmount(0);
        cart.setFinalAmount(0);
        return cartRepository.save(cart);
    }

    @Override
    public CartResponseDto addItemToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Integer priceInCents = convertToCents(product.getSellingPrice());

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(priceInCents * existingItem.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(priceInCents);
            newItem.setTotalPrice(priceInCents * quantity);
            cart.getItems().add(newItem);
        }

        recalculateCartTotals(cart);
        cartRepository.save(cart);

        return convertToDto(cart);
    }

    @Override
    public CartResponseDto removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        recalculateCartTotals(cart);
        cartRepository.save(cart);

        return convertToDto(cart);
    }

    @Override
    public CartResponseDto updateItemQuantity(Long cartId, Long itemId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
            item.setTotalPrice(item.getPrice() * quantity);
        }

        recalculateCartTotals(cart);
        cartRepository.save(cart);

        return convertToDto(cart);
    }

    @Override
    public CartResponseDto getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return convertToDto(cart);
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cart.setAppliedCoupon(null);
        cart.setSubtotal(0);
        cart.setDiscountAmount(0);
        cart.setFinalAmount(0);
        cartRepository.save(cart);
    }

    @Override
    public void applyCouponToCart(Long cartId, Coupon coupon, Integer discountAmount, Integer finalAmount) {
        cartRepository.updateAppliedCoupon(cartId, coupon, discountAmount, finalAmount);
    }

    @Override
    public void removeCouponFromCart(Long cartId) {
        cartRepository.removeAppliedCoupon(cartId);
    }

    @Override
    public Integer calculateCartTotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cart.getSubtotal();
    }

    private void recalculateCartTotals(Cart cart) {
        Integer subtotal = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(0, Integer::sum);

        cart.setSubtotal(subtotal);

        if (cart.getAppliedCoupon() != null) {
            cart.setFinalAmount(subtotal - cart.getDiscountAmount());
        } else {
            cart.setDiscountAmount(0);
            cart.setFinalAmount(subtotal);
        }

        if (cart.getFinalAmount() < 0) {
            cart.setFinalAmount(0);
        }
    }

    private CartResponseDto convertToDto(Cart cart) {
        CartResponseDto dto = new CartResponseDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setSubtotal(cart.getSubtotal());
        dto.setDiscountAmount(cart.getDiscountAmount());
        dto.setFinalAmount(cart.getFinalAmount());

        if (cart.getAppliedCoupon() != null) {
            dto.setAppliedCouponCode(cart.getAppliedCoupon().getCode());
        }

        dto.setItems(cart.getItems().stream()
                .map(this::convertToItemDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private CartItemDto convertToItemDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductImage(item.getProduct().getThumbnailUrl());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }
}