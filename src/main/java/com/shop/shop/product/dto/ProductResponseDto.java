package com.shop.shop.product.dto;

import com.shop.shop.brand.dto.BrandDto;
import com.shop.shop.category.dto.CategoryDto;
import com.shop.shop.supplier.dto.SupplierDto;
import com.shop.shop.unit.dto.UnitDto;
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
public class ProductResponseDto {
    private Long id;
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
    private BigDecimal effectivePrice;
    private BigDecimal wholesalePrice;
    private BigDecimal mrp;
    private BigDecimal taxAmount;
    private BigDecimal taxPercentage;
    private BigDecimal shippingCost;

    // Inventory
    private Integer quantityInStock;
    private Integer reservedQuantity;
    private Integer soldQuantity;
    private Integer returnedQuantity;
    private Integer damagedQuantity;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;
    private String stockStatus;
    private String availabilityStatus;
    private LocalDateTime expectedRestockDate;

    // Physical Attributes
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private String weightUnit;
    private String dimensionUnit;
    private String color;
    private String size;
    private String material;

    // Relationships
    private CategoryDto category;
    private BrandDto brand;
    private SupplierDto supplier;
    private UnitDto unit;

    // Media
    private List<String> imageUrls;
    private String thumbnailUrl;
    private String videoUrl;
    private List<String> documentUrls;

    // Content
    private String specifications;
    private String features;

    // SEO
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private String slug;

    // Status
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isNewArrival;
    private Boolean isBestSeller;
    private Boolean isOnSale;
    private Boolean isDigital;
    private Boolean isDownloadable;
    private Boolean isReturnable;
    private Boolean isTaxable;
    private Boolean isFreeShipping;
    private Boolean isPublished;
    private Boolean isApproved;
    private String approvalStatus;

    // Warranty
    private Integer warrantyMonths;
    private String warrantyTerms;
    private Integer guaranteeDays;
    private String returnPolicy;

    // Stats
    private Integer viewCount;
    private Integer purchaseCount;
    private Integer wishlistCount;
    private Integer reviewCount;
    private Double averageRating;
    private BigDecimal commissionPercentage;

    // Variations
    private Boolean hasVariations;
    private Map<String, List<String>> variationAttributes;
    private List<ProductVariationGetDto> variations;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private LocalDateTime approvedAt;
}
