package com.shop.shop.cart.controller;

import com.shop.shop.cart.dto.CartResponseDto;
import com.shop.shop.cart.entity.Cart;
import com.shop.shop.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carts")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})

public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponseDto> addItemToCart(
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, productId, quantity));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponseDto> removeItemFromCart(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartId, itemId));
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponseDto> updateItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(cartId, itemId, quantity));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<CartResponseDto> createCart(
            @RequestParam("userId") Optional<Long> userId,
            @RequestParam("sessionId") Optional<String> sessionId) {

        System.out.println("Received userId: " + userId);
        System.out.println("Received sessionId: " + sessionId);

        Cart cart = cartService.getOrCreateCart(userId.orElse(null), sessionId.orElse(null));
        return ResponseEntity.ok(cartService.getCart(cart.getId()));
    }
}