package com.shop.shop.slider.service;

import com.shop.shop.slider.dto.SliderCreateDto;
import com.shop.shop.slider.entity.Slider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SliderService {
    Slider create(SliderCreateDto dto);
    Page<Slider> getSliderListPaginated(String query, Pageable pageable);
    List<Slider> getSliderList();
    Slider deleteSlider(Long id);
}
