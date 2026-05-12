package com.shop.shop.order.entity;

import com.shop.shop.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productImage;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private Integer unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private Integer totalPrice;

    @Column(precision = 10, scale = 2)
    private Integer discountPrice;

    // Calculate total for this item
    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        if (discountPrice != null && discountPrice.compareTo(0) > 0) {
            this.totalPrice = discountPrice * quantity;
        } else {
            this.totalPrice = unitPrice * quantity;
        }
    }
}
