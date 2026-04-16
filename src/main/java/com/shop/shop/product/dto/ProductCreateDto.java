package com.shop.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {
    // Basic Information
    private String name;
    private String shortDescription;
    private String longDescription;
    private String sku;
    private String barcode;
    private String qrCode;

    // Pricing
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal discountPrice;
    private BigDecimal discountPercentage;
    private BigDecimal wholesalePrice;
    private BigDecimal mrp;
    private BigDecimal taxPercentage;
    private BigDecimal shippingCost;

    // Inventory
    private Integer quantityInStock;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;

    // Physical Attributes
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private String weightUnit;
    private String dimensionUnit;
    private String color;
    private String material;

    // Relationships
    private Long categoryId;
    private Long brandId;
    private Long supplierId;

    // Media
    private List<String> imageUrls;
    private String thumbnailUrl;
    private String videoUrl;

    // SEO
    private String seoTitle;
    private String seoDescription;
    private String slug;

    // Status
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isNewArrival;
    private Boolean isDigital;
    private Boolean isPublished;

    // Variations
    private Boolean hasVariations;
    private String variationAttributes; // JSON string of variation types
}
