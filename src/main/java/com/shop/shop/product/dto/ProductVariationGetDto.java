package com.shop.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationGetDto {
    private Long id;
    private String sku;
    private String barcode;
    private String upc;
    private String mpn;

    // Variation Attributes
    private Map<String, String> attributes;
    private String variationName;
    private String displayName;

    // Pricing
    private BigDecimal additionalPrice;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal effectivePrice;
    private BigDecimal purchasePrice;
    private BigDecimal wholesalePrice;
    private BigDecimal mrp;

    // Inventory
    private Integer quantityInStock;
    private Integer reservedQuantity;
    private Integer soldQuantity;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;
    private String stockStatus;
    private String availabilityStatus;

    // Physical Attributes
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private String weightUnit;
    private String dimensionUnit;

    // Media
    private List<String> imageUrls;
    private String thumbnailUrl;

    // Shipping
    private BigDecimal shippingCost;
    private Integer handlingDays;

    // Status
    private Boolean isActive;
    private Boolean isDefault;
    private Boolean isOnSale;

    // Stats
    private Integer viewCount;
    private Integer purchaseCount;
    private Integer wishlistCount;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
