package com.shop.shop.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_variations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Parent Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Variation Identification
    @Column(nullable = false)
    private String sku; // Unique SKU for this variation

    @Column(unique = true)
    private String barcode; // Unique barcode for this variation

    @Column(unique = true)
    private String upc; // Universal Product Code

    @Column(unique = true)
    private String mpn; // Manufacturer Part Number

    // Variation Attributes (Dynamic)
    @Column(columnDefinition = "JSON")
    private String attributes; // e.g., {"Color": "Red", "Size": "Large"}

    @Column(length = 100)
    private String variationName; // e.g., "Red - Large"

    @Column(length = 50)
    private String attribute1Name; // e.g., "Color"

    private String attribute1Value; // e.g., "Red"

    @Column(length = 50)
    private String attribute2Name; // e.g., "Size"

    private String attribute2Value; // e.g., "Large"

    @Column(length = 50)
    private String attribute3Name; // e.g., "Material"

    private String attribute3Value; // e.g., "Cotton"

    // Pricing (Can override parent product pricing)
    @Column(precision = 10, scale = 2)
    private BigDecimal additionalPrice; // Additional price over base product

    @Column(precision = 10, scale = 2)
    private BigDecimal price; // Specific price for this variation

    @Column(precision = 10, scale = 2)
    private BigDecimal discountPrice; // Discounted price for this variation

    @Column(precision = 10, scale = 2)
    private BigDecimal purchasePrice; // Cost price for this variation

    @Column(precision = 10, scale = 2)
    private BigDecimal wholesalePrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal mrp;

    // Inventory
    @Column(nullable = false)
    private Integer quantityInStock = 0;

    private Integer reservedQuantity = 0;

    private Integer soldQuantity = 0;

    private Integer minimumStockLevel;

    private Integer maximumStockLevel;

    @Column(length = 20)
    private String stockStatus; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK, BACKORDER

    // Physical Attributes
    private Double weight; // Can override product weight
    private Double length;
    private Double width;
    private Double height;

    @Column(length = 20)
    private String weightUnit;

    @Column(length = 20)
    private String dimensionUnit;

    // Media
    private String thumbnailUrl;

    @ElementCollection
    @CollectionTable(name = "variation_images",
            joinColumns = @JoinColumn(name = "variation_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    // Shipping
    @Column(precision = 10, scale = 2)
    private BigDecimal shippingCost;

    private Integer handlingDays;

    // Status
    private Boolean isActive = true;

    private Boolean isDefault = false; // Default variation when product is selected

    private Boolean isOnSale = false;

    @Column(length = 50)
    private String availabilityStatus; // IN_STOCK, PRE_ORDER, DISCONTINUED

    // Sales Metrics
    private Integer viewCount = 0;
    private Integer purchaseCount = 0;
    private Integer wishlistCount = 0;

    // Timestamps
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Version for optimistic locking
    @Version
    private Long version;

    // Helper methods
    public BigDecimal getEffectivePrice() {
        if (discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) > 0) {
            return discountPrice;
        }
        if (price != null) {
            return price;
        }
        if (product != null && product.getSellingPrice() != null) {
            return product.getSellingPrice().add(additionalPrice != null ? additionalPrice : BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    public String getDisplayName() {
        if (variationName != null && !variationName.isEmpty()) {
            return variationName;
        }
        StringBuilder display = new StringBuilder();
        if (attribute1Value != null) display.append(attribute1Value);
        if (attribute2Value != null) display.append(" - ").append(attribute2Value);
        if (attribute3Value != null) display.append(" - ").append(attribute3Value);
        return display.toString();
    }
}
