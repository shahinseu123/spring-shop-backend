package com.shop.shop.cart.entity;

import com.shop.shop.coupon.entity.Coupon;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "session_id")
    private String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // Coupon fields
    @ManyToOne
    @JoinColumn(name = "applied_coupon_id")
    private Coupon appliedCoupon;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private Integer discountAmount = 0;

    @Column(name = "final_amount", precision = 10, scale = 2)
    private Integer finalAmount;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private Integer subtotal = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
