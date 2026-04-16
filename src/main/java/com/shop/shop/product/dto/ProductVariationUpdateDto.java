package com.shop.shop.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationUpdateDto {
    private Long id;
    private String sku;
    private String barcode;
    private Map<String, String> attributes;
    private String variationName;

    // Pricing
    private BigDecimal additionalPrice;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal purchasePrice;
    private BigDecimal wholesalePrice;
    private BigDecimal mrp;

    // Inventory
    private Integer quantityInStock;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;
    private String stockStatus;

    // Physical Attributes
    private Double weight;
    private Double length;
    private Double width;
    private Double height;

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
    private String availabilityStatus;
}