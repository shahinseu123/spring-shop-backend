package com.shop.shop.coupon.service;

import com.shop.shop.coupon.dto.*;
import com.shop.shop.coupon.entity.Coupon;
import com.shop.shop.coupon.entity.CouponStatus;
import com.shop.shop.coupon.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {
    Void create(CouponCreateDto dto);
    Page<CouponRepository.CouponProjection> paginatedCoupon(Pageable pageable, String query);
    List<Coupon> couponList(String query);
    void deleteCoupon(Long id);
    CouponResponseDto updateCouponStatus(Long id, CouponStatus status);
    CouponValidationResponse validateCoupon(CouponValidationRequest dto);
    AppliedCouponResponseDto applyCouponToCart(String couponCode,Long cartId, Long userId);
    AppliedCouponResponseDto getAppliedCoupon(Long cartId);
    void removeCouponFromCart(Long cartId);
    Page<CouponResponseDto> getAvailableCoupons(Integer cartTotal, List<Long> productIds,Pageable pageable);
}
