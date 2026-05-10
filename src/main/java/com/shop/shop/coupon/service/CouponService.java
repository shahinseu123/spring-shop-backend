package com.shop.shop.coupon.service;

import com.shop.shop.coupon.dto.CouponCreateDto;
import com.shop.shop.coupon.entity.Coupon;

public interface CouponService {
    Void create(CouponCreateDto dto);
}
