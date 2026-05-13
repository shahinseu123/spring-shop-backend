package com.shop.shop.order.entity;

import com.shop.shop.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "discount_price")
    private Integer discountPrice;

    // Manual calculation method
    public Integer getTotalPrice() {
        if (this.totalPrice == null) {
            calculateTotalPrice();
        }
        return this.totalPrice;
    }

    public void calculateTotalPrice() {
        int price = (discountPrice != null && discountPrice > 0) ? discountPrice : unitPrice;
        this.totalPrice = price * (quantity != null ? quantity : 0);
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        calculateTotalPrice();
    }
}