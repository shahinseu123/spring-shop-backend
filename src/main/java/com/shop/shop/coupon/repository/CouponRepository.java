package com.shop.shop.coupon.repository;

import com.shop.shop.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsCouponByCode(String code);
    Optional<Coupon> findByCode(String code);
    @Query("""
SELECT c FROM Coupon c
WHERE(:query IS NULL OR :query = '' OR LOWER(c.code) LIKE CONCAT('%', LOWER(:query), '%') ) 
""")
    List<Coupon> findAllCoupon(@Param("query") String query);
    @Query("""
     SELECT c.id as id, 
            c.code as code, 
            c.discountType as discountType, 
            c.discountValue as discountValue,
            c.minimumOrderAmount as minimumOrderAmount,
            c.maximumDiscountAmount as maximumDiscountAmount,
            c.validFrom as validFrom,
            c.validUntil as validUntil,
            c.usageLimit as usageLimit,
            c.usageCount as usageCount,
            c.perUserLimit as perUserLimit,
            c.status as status
            FROM Coupon c
            WHERE(:query IS NULL OR :query = '' OR LOWER(c.code) LIKE CONCAT('%', LOWER(:query), '%'))
            
     
""")
    Page<CouponProjection> findAllPaginatedCoupon(@Param("query") String query, Pageable pageable);


    @Query("SELECT c FROM Coupon c WHERE c.status = 'ACTIVE' " +
            "AND c.validFrom <= :currentDate " +
            "AND c.validUntil >= :currentDate " +
            "AND (c.usageLimit IS NULL OR c.usageCount < c.usageLimit)")
    Page<Coupon> findAvailableCoupons(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    interface CouponProjection {
        Long getId();
        String getCode();
        String getDescription();
        String getDiscountType();
        Integer getDiscountValue();
        Integer getMinimumOrderAmount();
        Integer getMaximumDiscountAmount();
        LocalDate getValidFrom();
        LocalDate getValidUntil();
        Integer getUsageLimit();
        Integer getUsageCount();
        Integer getPerUserLimit();
        Integer getStatus();
    }


}
