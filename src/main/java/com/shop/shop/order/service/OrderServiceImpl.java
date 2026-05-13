package com.shop.shop.order.service;

import com.shop.shop.cart.entity.Cart;
import com.shop.shop.cart.entity.CartItem;
import com.shop.shop.cart.repository.CartRepository;
import com.shop.shop.order.dto.OrderCreateDto;
import com.shop.shop.order.dto.OrderItemDto;
import com.shop.shop.order.dto.OrderResponseDto;
import com.shop.shop.order.dto.ShippingAddressDto;
import com.shop.shop.order.entity.*;
import com.shop.shop.order.repository.OrderRepository;
import com.shop.shop.user.entity.User;
import com.shop.shop.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(CartRepository cartRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderCreateDto dto) {

        // 1. Get the cart
        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + dto.getCartId()));

        // 2. Validate cart has items
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot create order.");
        }

        // 3. Get the user from cart
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + cart.getUserId()));

        // 4. Build Shipping Address
        ShippingAddress shippingAddress = ShippingAddress.builder()
                .address(dto.getAddress())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .build();

        // 5. Generate order number
        String orderNumber = generateOrderNumber();

        // 6. Create order items from cart items
        List<OrderItem> orderItems = cart.getItems().stream()
//                .map(cartItem -> createOrderItem(cartItem))
                .map(this::createOrderItem)
                .toList();
// Get coupon info from cart
        String couponCode = null;
        Integer couponDiscount = 0;

        if (cart.getAppliedCoupon() != null) {
            couponCode = cart.getAppliedCoupon().getCode();
        }

        // The discount amount in your cart already includes coupon discount
        // So use cart.getDiscountAmount() as the total discount
        Integer totalDiscount = cart.getDiscountAmount() != null ? cart.getDiscountAmount() : 0;
        // 7. Build the order
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .shippingAddress(shippingAddress)
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .subtotal(cart.getSubtotal() != null ? cart.getSubtotal() : 0)
                .shippingCost(calculateShipping(cart.getSubtotal()))
                .discountAmount(totalDiscount)  // Total discount (product + coupon)
                .couponCode(couponCode)         // Just the coupon code
                .couponDiscount(totalDiscount)   // Total discount amount
                .paymentMethod(dto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .orderStatus(OrderStatus.PENDING)
                .notes(dto.getNotes())
                .build();

        // 8. Add order items to order
        orderItems.forEach(order::addOrderItem);

        // 9. Calculate totals
        order.calculateTotals();

        // 10. Handle payment method
        if (dto.getPaymentMethod() == PaymentMethod.ONLINE_PAYMENT && dto.getCardDetails() != null) {
            // Process online payment
            processOnlinePayment(order, dto);
        }

        // 11. Save the order
        Order savedOrder = orderRepository.save(order);

        // 12. Clear the cart after order is placed
        clearCart(cart);

        // 13. Return response
        return mapToResponseDto(savedOrder);
    }

    private OrderItem createOrderItem(CartItem cartItem) {
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .productName(cartItem.getProduct().getName())
                .productImage(cartItem.getProduct().getThumbnailUrl())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getPrice())
                .discountPrice(cartItem.getDiscountPrice())
                .build();
    }

    private String generateOrderNumber() {
        // Format: ORD-YYYYMMDD-XXXXX
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%05d", new Random().nextInt(99999));
        return "ORD-" + datePart + "-" + randomPart;
    }

    private Integer calculateShipping(Integer subtotal) {
        // Free shipping for orders above 50
        return subtotal >= 50 ? 0 : 10;
    }

    private void processOnlinePayment(Order order, OrderCreateDto dto) {
        // TODO: Integrate with payment gateway (Stripe, PayPal, etc.)
        // For now, simulate successful payment
        String transactionId = "TXN-" + System.currentTimeMillis();
        order.markAsPaid(transactionId);
    }

    private void clearCart(Cart cart) {
        // Clear all items
        cart.getItems().clear();

        // Reset financial fields
        cart.setSubtotal(0);
        cart.setDiscountAmount(0);
        cart.setFinalAmount(0);

        // Remove applied coupon
        cart.setAppliedCoupon(null);

        // Save the cleared cart
        cartRepository.save(cart);
    }

    private OrderResponseDto mapToResponseDto(Order order) {
        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(item -> OrderItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProductName())
                        .productImage(item.getProductImage())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .discountPrice(item.getDiscountPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .userName(order.getUser().getName())
                .email(order.getEmail())
                .phone(order.getPhone())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .shippingAddress(ShippingAddressDto.builder()
                        .address(order.getShippingAddress().getAddress())
                        .city(order.getShippingAddress().getCity())
                        .postalCode(order.getShippingAddress().getPostalCode())
                        .country(order.getShippingAddress().getCountry())
                        .build())
                .subtotal(order.getSubtotal())
                .shippingCost(order.getShippingCost())
                .discountAmount(order.getDiscountAmount())
                .couponDiscount(order.getCouponDiscount())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .transactionId(order.getTransactionId())
                .couponCode(order.getCouponCode())
                .items(itemDtos)
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .paidAt(order.getPaidAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .cancelledAt(order.getCancelledAt())
                .build();
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<OrderResponseDto> getUserOrders(Long userId) {
        return List.of();
    }

    @Override
    public void cancelOrder(Long orderId) {

    }
}
