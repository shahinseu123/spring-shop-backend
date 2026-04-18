package com.shop.shop.brand.service;

import com.shop.shop.IsActive;
import com.shop.shop.brand.dto.BrandDto;
import com.shop.shop.brand.entity.Brand;
import com.shop.shop.brand.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService{
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandDto create(BrandDto dto) {
        Brand newBrand = new Brand();
        newBrand.setName(dto.getName());
        newBrand.setLogoUrl(dto.getLogoUrl());
        newBrand.setSlug(dto.getName().toLowerCase());
        newBrand.setActive(IsActive.TRUE);
        Brand result = brandRepository.save(newBrand);
        dto.setId(result.getId());
        dto.setName(result.getName());
        dto.setSlug(result.getSlug());
        dto.setActive(result.getActive());
        dto.setLogoUrl(result.getLogoUrl());
        return dto;
    }

    @Override
    public Brand update(BrandDto dto, Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            brand.setName(dto.getName());
            brand.setSlug(dto.getName().toLowerCase());
        }

        if (dto.getLogoUrl() != null && !dto.getLogoUrl().isEmpty()) {
            brand.setLogoUrl(dto.getLogoUrl());
        }
        return brandRepository.save(brand);
    }

    @Override
    public Brand delete(Long brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new RuntimeException("Brand not found") );
        brandRepository.delete(brand);
        return brand;
    }

    @Override
    public Brand details(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(() -> new RuntimeException("Brand not found") );
    }

    @Override
    public Page<BrandRepository.BrandProjection> paginatedBrandList(Pageable pageable, String query) {
        return brandRepository.findAllBrand(query, pageable);
    }

    @Override
    public List<BrandRepository.BrandProjection> brandList(String query) {
        return brandRepository.findAllBrandList(query);
    }
}
