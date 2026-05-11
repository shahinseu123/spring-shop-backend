package com.shop.shop.cart.repository;

import com.shop.shop.cart.entity.Cart;
import com.shop.shop.coupon.entity.Coupon;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByAppliedCouponId(Long couponId);

    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findBySessionId(String sessionId);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.appliedCoupon = null, c.discountAmount = 0, c.finalAmount = c.subtotal WHERE c.id = :cartId")
    void removeAppliedCoupon(@Param("cartId") Long cartId);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.appliedCoupon = :couponId, c.discountAmount = :discountAmount, c.finalAmount = :finalAmount WHERE c.id = :cartId")
    void updateAppliedCoupon(@Param("cartId") Long cartId,
                             @Param("couponId") Coupon couponId,
                             @Param("discountAmount") Integer discountAmount,
                             @Param("finalAmount") Integer finalAmount);
}
