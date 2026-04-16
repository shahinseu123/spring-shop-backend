package com.shop.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetDto {
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String sku;
    private String barcode;

    // Pricing
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal discountPrice;
    private BigDecimal effectivePrice;
    private BigDecimal mrp;
    private BigDecimal taxPercentage;

    // Inventory
    private Integer quantityInStock;
    private Integer reservedQuantity;
    private Integer soldQuantity;
    private String stockStatus;
    private String availabilityStatus;

    // Physical Attributes
    private Double weight;
    private String color;
    private String material;

    // Relationships
    private String categoryName;
    private String brandName;
    private String supplierName;

    // Media
    private List<String> imageUrls;
    private String thumbnailUrl;

    // Stats
    private Integer viewCount;
    private Integer purchaseCount;
    private Integer reviewCount;
    private Double averageRating;

    // Status
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isNewArrival;
    private Boolean isBestSeller;
    private Boolean isOnSale;
    private Boolean isDigital;

    // Variations
    private Boolean hasVariations;
    private Integer variationCount;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
