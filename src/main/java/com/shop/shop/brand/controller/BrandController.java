package com.shop.shop.brand.controller;


import com.shop.shop.brand.dto.BrandDto;
import com.shop.shop.brand.entity.Brand;
import com.shop.shop.brand.repository.BrandRepository;
import com.shop.shop.brand.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<?> createBrand(BrandDto brandDto){
       return new ResponseEntity<>(brandService.create(brandDto),HttpStatus.OK);
    }

    @GetMapping("/details")
    public ResponseEntity<Brand> brandDetails(@RequestParam("id") Long brandId){
       return new ResponseEntity<>(brandService.details(brandId),HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Brand> updateBrand(@RequestBody BrandDto brandDto, @RequestParam("id") Long brandId){
       return new ResponseEntity<>(brandService.update(brandDto, brandId),HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Brand> deleteBrand(@RequestParam("id") Long brandId){
       return new ResponseEntity<>(brandService.delete(brandId),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BrandRepository.BrandProjection>> paginatedBrandList(Pageable pageable, @RequestParam("query") Optional<String> query){
       return new ResponseEntity<>(brandService.paginatedBrandList(pageable, query.orElse(null)),HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<BrandRepository.BrandProjection>> brandList(@RequestParam("query") Optional<String> query){
       return new ResponseEntity<>(brandService.brandList(query.orElse(null)),HttpStatus.OK);
    }

}
