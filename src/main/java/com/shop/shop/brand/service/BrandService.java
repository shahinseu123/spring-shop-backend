package com.shop.shop.brand.service;

import com.shop.shop.brand.dto.BrandDto;
import com.shop.shop.brand.entity.Brand;
import com.shop.shop.brand.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    BrandDto create(BrandDto dto);
    Brand update(BrandDto dto, Long brandId);
    Brand delete(Long brandId);
    Brand details(Long brandId);
    Page<BrandRepository.BrandProjection> paginatedBrandList(Pageable pageable, String query);
    List<BrandRepository.BrandProjection> brandList(String query);
}
