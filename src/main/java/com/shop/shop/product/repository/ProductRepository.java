package com.shop.shop.product.repository;

import com.shop.shop.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
    SELECT 
       p.id as id,
       p.name as name,
       p.slug as slug,
       p.sellingPrice as sellingPrice,
       p.thumbnailUrl as thumbnailUrl,
       b.name as brandName,
       b.logoUrl as brandLogoUrl
    FROM Product p
    LEFT JOIN p.brand b
    WHERE(:query IS NULL OR :query = "" OR LOWER(p.name) LIKE CONCAT("%",LOWER(:query), "%"))
""")
    Page<ProductProjection> findAllProducts(@Param("query") String query, Pageable pageable);

    interface ProductProjection {
            Long getId();
            String getName();
            String getSlug();
            BigDecimal getSellingPrice();
            String getThumbnailUrl();
            // Brand fields
            String getBrandName();
            String getBrandLogoUrl();
    }

}
