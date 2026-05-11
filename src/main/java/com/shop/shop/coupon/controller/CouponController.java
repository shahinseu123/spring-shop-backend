package com.shop.shop.coupon.controller;

import com.shop.shop.coupon.dto.*;
import com.shop.shop.coupon.entity.CouponStatus;
import com.shop.shop.coupon.repository.CouponRepository;
import com.shop.shop.coupon.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping
    public ResponseEntity<Page<CouponRepository.CouponProjection>> paginatedCoupon(@RequestParam("search") Optional<String> query, Pageable pageable){
        return new ResponseEntity<>(couponService.paginatedCoupon(pageable, query.orElse(null)), HttpStatus.OK);
    }
    @PostMapping("/list")
    public ResponseEntity<?> couponList(@RequestParam("search") Optional<String> query){
        return new ResponseEntity<>(couponService.couponList(query.orElse(null)), HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> couponList(@PathVariable("id") Long id){
        couponService.deleteCoupon(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CouponResponseDto> updateCouponStatus(@PathVariable Long id,
                                                                @RequestParam String status) {
        return ResponseEntity.ok(couponService.updateCouponStatus(id, CouponStatus.valueOf(status)));
    }
    @PostMapping("/validate")
    public ResponseEntity<CouponValidationResponse> validateCoupon(@RequestBody CouponValidationRequest request) {
        return ResponseEntity.ok(couponService.validateCoupon(request));
    }
    @PostMapping("/apply/{couponCode}")
    public ResponseEntity<AppliedCouponResponseDto> applyCoupon(
            @PathVariable String couponCode,
            @RequestParam Long cartId,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(couponService.applyCouponToCart(couponCode, cartId, userId));
    }

    @GetMapping("/applied/{cartId}")
    public ResponseEntity<AppliedCouponResponseDto> getAppliedCoupon(@PathVariable Long cartId) {
        return ResponseEntity.ok(couponService.getAppliedCoupon(cartId));
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<Void> removeCoupon(@PathVariable Long cartId) {
        couponService.removeCouponFromCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<Page<CouponResponseDto>> getAvailableCoupons(
            @RequestParam(required = false) Integer cartTotal,
            @RequestParam(required = false) List<Long> productIds,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(couponService.getAvailableCoupons(cartTotal, productIds, pageable));
    }



}
