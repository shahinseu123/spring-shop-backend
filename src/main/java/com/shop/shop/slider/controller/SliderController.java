package com.shop.shop.slider.controller;

import com.shop.shop.slider.dto.SliderCreateDto;
import com.shop.shop.slider.entity.Slider;
import com.shop.shop.slider.service.SliderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sliders")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})

public class SliderController {
    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @PostMapping
    public ResponseEntity<Slider> createSlider(@RequestBody SliderCreateDto dto) {
        return new ResponseEntity<>(sliderService.create(dto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Slider>> sliderListPaginated(@RequestParam("search") Optional<String> query, Pageable pageable) {
        return new ResponseEntity<>(sliderService.getSliderListPaginated(query.orElse(null), pageable), HttpStatus.OK);
    }
    @GetMapping("/list")
    public ResponseEntity<List<Slider>> sliderList() {
        return new ResponseEntity<>(sliderService.getSliderList(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSlide(@PathVariable("id") Long id) {
        return new ResponseEntity<>(sliderService.deleteSlider(id), HttpStatus.OK);
    }



}
