package com.shop.shop.product.service;

import com.shop.shop.brand.entity.Brand;
import com.shop.shop.brand.repository.BrandRepository;
import com.shop.shop.category.entity.Category;
import com.shop.shop.category.repository.CategoryRepository;
import com.shop.shop.product.*;
import com.shop.shop.product.dto.ProductCreateDto;
import com.shop.shop.product.entity.Product;
import com.shop.shop.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public ProductServiceImpl(CategoryRepository categoryRepository,
                              BrandRepository brandRepository,
                              ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product create(ProductCreateDto dto) {
        // Check if product with same SKU or name already exists
        if (productRepository.existsBySku(dto.getName().toLowerCase())) {
            throw new RuntimeException("Product with same SKU already exists");
        }

        Product newProduct = new Product();

        // Basic Information
        newProduct.setName(dto.getName());
        newProduct.setShortDescription(dto.getShortDescription());
        newProduct.setLongDescription(dto.getLongDescription());
        newProduct.setSku(dto.getName().toLowerCase());
        newProduct.setBarcode(dto.getBarcode());
        newProduct.setQrCode(dto.getQrCode());

        // Pricing
        newProduct.setPurchasePrice(dto.getPurchasePrice());
        newProduct.setSellingPrice(dto.getSellingPrice());
        newProduct.setDiscountPrice(dto.getDiscountPrice());
        newProduct.setDiscountPercentage(dto.getDiscountPercentage());
        newProduct.setWholesalePrice(dto.getWholesalePrice());
        newProduct.setMrp(dto.getMrp());
        newProduct.setTaxPercentage(dto.getTaxPercentage());
        newProduct.setShippingCost(dto.getShippingCost());

        // Inventory
        newProduct.setQuantityInStock(dto.getQuantityInStock());
        newProduct.setMinimumStockLevel(dto.getMinimumStockLevel());
        newProduct.setMaximumStockLevel(dto.getMaximumStockLevel());

        // Physical Attributes
        newProduct.setWeight(dto.getWeight());
        newProduct.setLength(dto.getLength());
        newProduct.setWidth(dto.getWidth());
        newProduct.setHeight(dto.getHeight());
        newProduct.setWeightUnit(dto.getWeightUnit());
        newProduct.setDimensionUnit(dto.getDimensionUnit());
        newProduct.setColor(dto.getColor());
        newProduct.setMaterial(dto.getMaterial());

        // Relationships - FIXED: Fetch actual entities instead of creating new ones
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            newProduct.setCategory(category);
        }

        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found with id: " + dto.getBrandId()));
            newProduct.setBrand(brand);
        }

        // Media
        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            newProduct.setImageUrls(dto.getImageUrls());
        }
        newProduct.setThumbnailUrl(dto.getThumbnailUrl());
        newProduct.setVideoUrl(dto.getVideoUrl());

        // SEO
        newProduct.setSeoTitle(dto.getSeoTitle());
        newProduct.setSeoDescription(dto.getSeoDescription());

        // Generate unique slug
        String baseSlug = generateSlug(dto.getName());
        String finalSlug = baseSlug;
        int counter = 1;
        while (productRepository.existsBySlug(finalSlug)) {
            finalSlug = baseSlug + "-" + counter;
            counter++;
        }
        newProduct.setSlug(finalSlug);

        // Status
        newProduct.setIsActive(IsActive.YES);
        newProduct.setIsFeatured(IsFeatured.YES);
        newProduct.setIsNewArrival(IsNewArrival.YES);
        newProduct.setIsDigital(IsDigital.YES);
        newProduct.setIsPublished(IsPublished.YES);
        newProduct.setHasVariations(HasVariations.YES);

        return productRepository.save(newProduct);
    }

    @Override
    public Page<ProductRepository.ProductProjection> paginatedProducts(Pageable pageable, String query) {

        return productRepository.findAllProducts(query, pageable);
    }

    @Override
    public ProductRepository.ProductDetailsProjection productDetails(Long id) {
        return productRepository.findProductDetails(id);
    }

    @Override
    @Transactional
    public Void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
        return null;
    }

    // Helper method to generate slug
    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}