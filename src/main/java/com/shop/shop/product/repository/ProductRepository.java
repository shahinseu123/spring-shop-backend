package com.shop.shop.product.repository;

import com.shop.shop.product.IsActive;
import com.shop.shop.product.IsFeatured;
import com.shop.shop.product.IsNewArrival;
import com.shop.shop.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Check for duplicate SKU
    boolean existsBySku(String sku);

    // Check for duplicate slug
    boolean existsBySlug(String slug);
    @Query("""
    SELECT 
       p.id as id,
       p.name as name,
       p.slug as slug,
       p.sellingPrice as sellingPrice,
       p.imageUrls as imageUrls,
       p.thumbnailUrl as thumbnailUrl,
       b.name as brandName,
       b.logoUrl as brandLogoUrl
    FROM Product p
    LEFT JOIN p.brand b
    WHERE(:query IS NULL OR :query = '' OR LOWER(p.name) LIKE CONCAT('%', LOWER(:query), '%'))
""")
    Page<ProductProjection> findAllProducts(@Param("query") String query, Pageable pageable);

    @SuppressWarnings("SqlNoDataSourceInspection")
    @Query(value = """
    SELECT 
        p.id,
        p.name,
        p.short_description as shortDescription,
        p.long_description as longDescription,
        p.sku,
        p.selling_price as sellingPrice,
        p.discount_price as discountPrice,
        p.discount_percentage as discountPercentage,
        p.mrp,
        p.quantity_in_stock as quantityInStock,
        p.stock_status as stockStatus,
        p.availability_status as availabilityStatus,
        p.thumbnail_url as thumbnailUrl,
        (SELECT JSON_ARRAYAGG(pi.image_url) FROM product_images pi WHERE pi.product_id = p.id) as imageUrls,
        p.is_active as isActive,
        p.is_featured as isFeatured,
        p.is_new_arrival as isNewArrival,
        p.average_rating as averageRating,
        p.review_count as reviewCount,
        b.id as brandId,
        b.name as brandName,
        b.logo_url as brandLogoUrl,
        c.id as categoryId,
        c.name as categoryName,
        c.slug as categorySlug
    FROM products p
    LEFT JOIN brands b ON p.brand_id = b.id
    LEFT JOIN categories c ON p.category_id = c.id
    WHERE p.id = :id
    GROUP BY p.id, b.id, c.id
    """, nativeQuery = true)
    ProductDetailsProjection findProductDetails(@Param("id") Long id);

    interface ProductDetailsProjection {
        Long getId();
        String getName();
        String getShortDescription();
        String getLongDescription();
        String getSku();
        BigDecimal getSellingPrice();
        BigDecimal getDiscountPrice();
        BigDecimal getDiscountPercentage();
        BigDecimal getMrp();
        Integer getQuantityInStock();
        String getStockStatus();
        String getAvailabilityStatus();
        String getThumbnailUrl();
        List<String> getImageUrls();
        Short getIsActive();
        Short getIsFeatured();
        Short getIsNewArrival();
        Double getAverageRating();
        Integer getReviewCount();

        // Brand fields
        Long getBrandId();
        String getBrandName();
        String getBrandLogoUrl();

        // Category fields
        Long getCategoryId();
        String getCategoryName();
        String getCategorySlug();
    }

    interface ProductProjection {
            Long getId();
            String getName();
            String getSlug();
            BigDecimal getSellingPrice();
            String getThumbnailUrl();
            List<String> getImageUrls();
            // Brand fields
            String getBrandName();
            String getBrandLogoUrl();
    }

}
