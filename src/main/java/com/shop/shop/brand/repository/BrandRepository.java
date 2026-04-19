package com.shop.shop.brand.repository;

import com.shop.shop.brand.dto.BrandDto;
import com.shop.shop.brand.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("""
      SELECT b.id as id, b.name as name, b.logoUrl as logoUrl FROM Brand b 
      WHERE(:query IS NULL  OR :query = '' OR LOWER(b.name) LIKE LOWER(CONCAT('%', LOWER(:query), '%')))
""")
    Page<BrandProjection> findAllBrand(@Param("query") String query, Pageable pageable);

    @Query("""
      SELECT b.id as id, b.name as name, b.logoUrl as logoUrl FROM Brand b 
      WHERE(:query IS NULL  OR :query = '' OR LOWER(b.name) LIKE LOWER(CONCAT('%', LOWER(:query), '%')))
""")
    List<BrandProjection> findAllBrandList(@Param("query") String query);

    @Query("""
      SELECT b.id as id, b.name as name, b.logoUrl as logoUrl, b.slug as slug  FROM Brand b 
      WHERE b.id = :id
""")
    BrandProjection findBrandDetailsById(@Param("id") Long id);

    interface BrandProjection {
        Long getId();
        String getName();
        String getLogoUrl();
        String getSlug();
    }

}

