package com.shop.shop.coupon.service;

import com.shop.shop.cart.entity.Cart;
import com.shop.shop.cart.repository.CartRepository;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import com.shop.shop.coupon.dto.*;
import com.shop.shop.coupon.entity.Coupon;
import com.shop.shop.coupon.entity.CouponStatus;
import com.shop.shop.coupon.entity.DiscountType;
import com.shop.shop.coupon.repository.CouponRepository;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CouponServiceImpl(CouponRepository couponRepository,
                             CategoryRepository categoryRepository,
                             ProductRepository productRepository,
                             CartRepository cartRepository) {
        this.couponRepository = couponRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Void create(CouponCreateDto dto) {
        DiscountType discountType = DiscountType.valueOf(dto.getDiscountType());

        // 1. Check if coupon code exists
        if (couponRepository.existsCouponByCode(dto.getCode().toUpperCase())) {
            throw new RuntimeException("Coupon code already exists: " + dto.getCode());
        }

        // 2. Validate dates
        if (dto.getValidFrom().isAfter(dto.getValidUntil())) {
            throw new RuntimeException("Valid from date cannot be after valid until date");
        }

        // 3. Validate discount value
        if (dto.getDiscountValue() <= 0) {
            throw new RuntimeException("Discount value must be greater than 0");
        }

        // 4. Validate percentage discount
        if (discountType == DiscountType.PERCENTAGE && dto.getDiscountValue() > 100) {
            throw new RuntimeException("Percentage discount must be between 1 and 100");
        }

        // 5. Validate minimum order amount
        if (dto.getMinimumOrderAmount() != null && dto.getMinimumOrderAmount() < 0) {
            throw new RuntimeException("Minimum order amount cannot be negative");
        }

        // 6. Validate usage limit
        if (dto.getUsageLimit() != null && dto.getUsageLimit() < 1) {
            throw new RuntimeException("Usage limit must be at least 1");
        }

        // 7. Validate per user limit
        if (dto.getPerUserLimit() != null && dto.getPerUserLimit() < 1) {
            throw new RuntimeException("Per user limit must be at least 1");
        }

        // 8. Create coupon
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(dto.getCode().toUpperCase());
        newCoupon.setDescription(dto.getDescription());
        newCoupon.setDiscountType(discountType);
        newCoupon.setDiscountValue(dto.getDiscountValue());
        newCoupon.setMaximumDiscountAmount(dto.getMaximumDiscountAmount());
        newCoupon.setMinimumOrderAmount(dto.getMinimumOrderAmount());
        newCoupon.setValidFrom(dto.getValidFrom());
        newCoupon.setValidUntil(dto.getValidUntil());
        newCoupon.setUsageLimit(dto.getUsageLimit());
        newCoupon.setPerUserLimit(dto.getPerUserLimit());
        newCoupon.setUsageCount(0);
        newCoupon.setStatus(CouponStatus.ACTIVE);

        // 9. Set applicable products
        if (dto.getApplicableProductIds() != null && !dto.getApplicableProductIds().isEmpty()) {
            Set<Product> applicableProducts = new HashSet<>();
            for (Long productId : dto.getApplicableProductIds()) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
                applicableProducts.add(product);
            }
            newCoupon.setApplicableProducts(applicableProducts);
        }

        // 10. Set applicable categories
        if (dto.getApplicableCategoryIds() != null && !dto.getApplicableCategoryIds().isEmpty()) {
            Set<Category> applicableCategories = new HashSet<>();
            for (Long categoryId : dto.getApplicableCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
                applicableCategories.add(category);
            }
            newCoupon.setApplicableCategories(applicableCategories);
        }

        couponRepository.save(newCoupon);
        log.info("Coupon created successfully: {}", dto.getCode());
        return null;
    }

    @Override
    public Page<CouponRepository.CouponProjection> paginatedCoupon(Pageable pageable, String query) {
        return couponRepository.findAllPaginatedCoupon(query, pageable);
    }

    @Override
    public List<Coupon> couponList(String query) {
        return couponRepository.findAllCoupon(query);
    }

    @Override
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));

        // Check if coupon is applied to any active cart
        boolean isUsedInCart = cartRepository.existsByAppliedCouponId(id);
        if (isUsedInCart) {
            throw new RuntimeException("Cannot delete coupon that is applied to active carts");
        }

        couponRepository.delete(coupon);
        log.info("Coupon deleted successfully with id: {}", id);
    }

    @Override
    public CouponResponseDto updateCouponStatus(Long id, CouponStatus status) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
        coupon.setStatus(status);
        Coupon saved = couponRepository.save(coupon);
        log.info("Coupon status updated: {} -> {}", saved.getCode(), status);
        return convertToDto(saved);
    }

    @Override
    public CouponValidationResponse validateCoupon(CouponValidationRequest request) {
        try {
            Coupon coupon = couponRepository.findByCode(request.getCouponCode().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Coupon not found"));

            String validationError = validateCouponEligibility(coupon, request);
            if (validationError != null) {
                return CouponValidationResponse.builder()
                        .valid(false)
                        .message(validationError)
                        .build();
            }

            // Calculate discount
            Integer discountAmount = calculateDiscount(coupon, request.getCartTotal());
            Integer finalAmount = request.getCartTotal() - discountAmount;

            return CouponValidationResponse.builder()
                    .valid(true)
                    .discountAmount(discountAmount)
                    .finalAmount(finalAmount)
                    .coupon(convertToDto(coupon))
                    .build();

        } catch (RuntimeException e) {
            log.warn("Coupon validation failed: {}", e.getMessage());
            return CouponValidationResponse.builder()
                    .valid(false)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("Unexpected error during coupon validation", e);
            return CouponValidationResponse.builder()
                    .valid(false)
                    .message("An unexpected error occurred")
                    .build();
        }
    }

    @Override
    public AppliedCouponResponseDto applyCouponToCart(String couponCode, Long cartId, Long userId) {
        // 1. Find coupon
        Coupon coupon = couponRepository.findByCode(couponCode.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        // 2. Find cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        // 3. Calculate cart total
        Integer cartTotal = calculateCartTotal(cart);

        // 4. Get cart items info for validation
        List<Long> productIds = getCartProductIds(cart);
        List<Long> categoryIds = getCartCategoryIds(cart);

        // 5. Validate coupon eligibility
        CouponValidationRequest validationRequest = new CouponValidationRequest();
        validationRequest.setCouponCode(couponCode);
        validationRequest.setCartTotal(cartTotal);
        validationRequest.setUserId(userId);
        validationRequest.setProductIds(productIds);
        validationRequest.setCategoryIds(categoryIds);

        CouponValidationResponse validation = validateCoupon(validationRequest);

        if (!validation.isValid()) {
            throw new RuntimeException(validation.getMessage());
        }

        // 6. Check if another coupon is already applied
        if (cart.getAppliedCoupon() != null && !cart.getAppliedCoupon().getId().equals(coupon.getId())) {
            throw new RuntimeException("Remove existing coupon before applying a new one");
        }

        // 7. Apply coupon to cart in database
        cart.setAppliedCoupon(coupon);
        cart.setDiscountAmount(validation.getDiscountAmount());
        cart.setFinalAmount(validation.getFinalAmount());
        cartRepository.save(cart);

        // 8. Increment usage count
        coupon.setUsageCount(coupon.getUsageCount() + 1);
        couponRepository.save(coupon);

        log.info("Coupon applied successfully: {} to cart: {}", couponCode, cartId);

        return AppliedCouponResponseDto.builder()
                .couponCode(coupon.getCode())
                .discountType(coupon.getDiscountType().name())
                .discountValue(coupon.getDiscountValue())
                .discountAmount(validation.getDiscountAmount())
                .originalAmount(cartTotal)
                .finalAmount(validation.getFinalAmount())
                .build();
    }

    @Override
    public AppliedCouponResponseDto getAppliedCoupon(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        if (cart.getAppliedCoupon() == null) {
            return null;
        }

        Coupon coupon = cart.getAppliedCoupon();
        return AppliedCouponResponseDto.builder()
                .couponCode(coupon.getCode())
                .discountType(coupon.getDiscountType().name())
                .discountValue(coupon.getDiscountValue())
                .discountAmount(cart.getDiscountAmount())
                .originalAmount(cart.getSubtotal())
                .finalAmount(cart.getFinalAmount())
                .build();
    }

    @Override
    public void removeCouponFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        if (cart.getAppliedCoupon() == null) {
            throw new RuntimeException("No coupon applied to this cart");
        }

        cart.setAppliedCoupon(null);
        cart.setDiscountAmount(0);
        cart.setFinalAmount(cart.getSubtotal());
        cartRepository.save(cart);

        log.info("Coupon removed from cart: {}", cartId);
    }

    @Override
    public Page<CouponResponseDto> getAvailableCoupons(Integer cartTotal, List<Long> productIds, Pageable pageable) {
        LocalDate now = LocalDate.now();
        return couponRepository.findAvailableCoupons(now, pageable)
                .map(this::convertToDto);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private String validateCouponEligibility(Coupon coupon, CouponValidationRequest request) {
        // Check status
        if (coupon.getStatus() != CouponStatus.ACTIVE) {
            return "Coupon is not active";
        }

        // Check date range
        LocalDate now = LocalDate.now();
        if (now.isBefore(coupon.getValidFrom())) {
            return "Coupon is not yet valid. Valid from: " + coupon.getValidFrom();
        }
        if (now.isAfter(coupon.getValidUntil())) {
            return "Coupon has expired. Valid until: " + coupon.getValidUntil();
        }

        // Check usage limit
        if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
            return "Coupon usage limit has been reached";
        }

        // Check minimum order amount
        if (coupon.getMinimumOrderAmount() != null &&
                request.getCartTotal() < coupon.getMinimumOrderAmount()) {
            return "Minimum order amount of $" + coupon.getMinimumOrderAmount() + " required. Your total: $" + request.getCartTotal();
        }

        // Check product/category applicability
        if (!isApplicableToCart(coupon, request.getProductIds(), request.getCategoryIds())) {
            return "Coupon is not applicable to items in your cart";
        }

        return null;
    }

    private boolean isApplicableToCart(Coupon coupon, List<Long> productIds, List<Long> categoryIds) {
        // If no specific products/categories, applicable to all
        if ((coupon.getApplicableProducts() == null || coupon.getApplicableProducts().isEmpty()) &&
                (coupon.getApplicableCategories() == null || coupon.getApplicableCategories().isEmpty())) {
            return true;
        }

        // Check if any cart product is in applicable products
        if (coupon.getApplicableProducts() != null && !coupon.getApplicableProducts().isEmpty() && productIds != null) {
            Set<Long> applicableProductIds = coupon.getApplicableProducts().stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());
            if (productIds.stream().anyMatch(applicableProductIds::contains)) {
                return true;
            }
        }

        // Check if any cart category is in applicable categories
        if (coupon.getApplicableCategories() != null && !coupon.getApplicableCategories().isEmpty() && categoryIds != null) {
            Set<Long> applicableCategoryIds = coupon.getApplicableCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
            if (categoryIds.stream().anyMatch(applicableCategoryIds::contains)) {
                return true;
            }
        }

        return false;
    }

    private Integer calculateDiscount(Coupon coupon, Integer cartTotal) {
        Integer discount;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = (cartTotal * coupon.getDiscountValue()) / 100;

            // Apply maximum discount limit if exists
            if (coupon.getMaximumDiscountAmount() != null && discount > coupon.getMaximumDiscountAmount()) {
                discount = coupon.getMaximumDiscountAmount();
            }
        } else { // FIXED_AMOUNT
            discount = coupon.getDiscountValue();
        }

        // Ensure discount doesn't exceed cart total
        if (discount > cartTotal) {
            discount = cartTotal;
        }

        return discount;
    }

    private Integer calculateCartTotal(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return 0;
        }
        return cart.getItems().stream()
                .map(item -> item.getTotalPrice())
                .reduce(0, Integer::sum);
    }

    private List<Long> getCartProductIds(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return new ArrayList<>();
        }
        return cart.getItems().stream()
                .map(item -> item.getProduct().getId())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> getCartCategoryIds(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return new ArrayList<>();
        }
        return cart.getItems().stream()
                .map(item -> item.getProduct().getCategory().getId())
                .distinct()
                .collect(Collectors.toList());
    }

    private CouponResponseDto convertToDto(Coupon coupon) {
        CouponResponseDto dto = new CouponResponseDto();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDescription(coupon.getDescription());
        dto.setDiscountType(coupon.getDiscountType().name());
        dto.setDiscountValue(coupon.getDiscountValue());
        dto.setMinimumOrderAmount(coupon.getMinimumOrderAmount());
        dto.setMaximumDiscountAmount(coupon.getMaximumDiscountAmount());
        dto.setValidFrom(coupon.getValidFrom());
        dto.setValidUntil(coupon.getValidUntil());
        dto.setUsageLimit(coupon.getUsageLimit());
        dto.setUsageCount(coupon.getUsageCount());
        dto.setPerUserLimit(coupon.getPerUserLimit());
        dto.setStatus(coupon.getStatus().name());

        if (coupon.getApplicableProducts() != null && !coupon.getApplicableProducts().isEmpty()) {
            dto.setApplicableProductIds(coupon.getApplicableProducts().stream()
                    .map(Product::getId)
                    .collect(Collectors.toList()));
        }

        if (coupon.getApplicableCategories() != null && !coupon.getApplicableCategories().isEmpty()) {
            dto.setApplicableCategoryIds(coupon.getApplicableCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}