package com.shop.shop.coupon.controller;

import com.shop.shop.coupon.dto.CouponCreateDto;
import com.shop.shop.coupon.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})

public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<Void> createCoupon(@RequestBody CouponCreateDto dto){
        return new ResponseEntity<>(couponService.create(dto), HttpStatus.OK);
    }

}
