package com.shop.shop.product.entity;


import com.shop.shop.brand.entity.Brand;
import com.shop.shop.category.entity.Category;
import com.shop.shop.supplier.entity.Supplier;
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
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Information
    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String shortDescription;

    @Column(length = 5000)
    private String longDescription;

    @Column(unique = true, length = 100)
    private String sku; // Stock Keeping Unit

    @Column(unique = true, length = 100)
    private String barcode;

    @Column(unique = true, length = 100)
    private String qrCode;

    // Pricing Information
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountPrice;

    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(precision = 10, scale = 2)
    private BigDecimal wholesalePrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal mrp;

    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    @Column(precision = 10, scale = 2)
    private BigDecimal shippingCost;

    // Inventory
    private Integer quantityInStock = 0;
    private Integer reservedQuantity = 0;
    private Integer soldQuantity = 0;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;

    @Column(length = 20)
    private String stockStatus; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK

    @Column(length = 20)
    private String availabilityStatus; // AVAILABLE, COMING_SOON, DISCONTINUED

    private Boolean hasVariations = false;

    // Physical Attributes
    private Double weight;
    private Double length;
    private Double width;
    private Double height;

    @Column(length = 20)
    private String weightUnit;

    @Column(length = 20)
    private String dimensionUnit;

    private String color;
    private String material;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    // Media
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    private String thumbnailUrl;

    private String videoUrl;

    // Variations - One product can have many variations
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductVariation> variations = new ArrayList<>();

    // Variation Attributes (JSON store of variation types)
    @Column(columnDefinition = "JSON")
    private String variationAttributes; // e.g., {"Color": ["Red", "Blue"], "Size": ["S", "M", "L"]}

    // SEO
    private String seoTitle;
    @Column(length = 500)
    private String seoDescription;
    private String slug;

    // Status Flags
    private Boolean isActive = true;
    private Boolean isFeatured = false;
    private Boolean isNewArrival = false;
    private Boolean isBestSeller = false;
    private Boolean isOnSale = false;
    private Boolean isDigital = false;
    private Boolean isPublished = true;

    // Sales Metrics
    private Integer viewCount = 0;
    private Integer purchaseCount = 0;
    private Integer wishlistCount = 0;
    private Integer reviewCount = 0;
    private Double averageRating = 0.0;

    // Timestamps
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    // Version
    @Version
    private Long version;

    // Helper methods
    public void addVariation(ProductVariation variation) {
        variations.add(variation);
        variation.setProduct(this);
        hasVariations = true;
    }

    public void removeVariation(ProductVariation variation) {
        variations.remove(variation);
        variation.setProduct(null);
        hasVariations = !variations.isEmpty();
    }

    public Integer getTotalStock() {
        if (hasVariations && variations != null) {
            return variations.stream()
                    .mapToInt(ProductVariation::getQuantityInStock)
                    .sum();
        }
        return quantityInStock;
    }
}