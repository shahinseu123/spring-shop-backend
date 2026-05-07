package com.shop.shop.slider.service;

import com.shop.shop.slider.dto.SliderCreateDto;
import com.shop.shop.slider.entity.Slider;
import com.shop.shop.slider.repository.SliderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SliderServiceImpl implements SliderService {
    private final SliderRepository sliderRepository;

    public SliderServiceImpl(SliderRepository sliderRepository) {
        this.sliderRepository = sliderRepository;
    }

    @Override
    public Slider create(SliderCreateDto dto) {
        Slider newSlider = new Slider();
        newSlider.setTitle(dto.getTitle());
        newSlider.setSubtitle(dto.getSubtitle());
        newSlider.setDescription(dto.getDescription());
        newSlider.setButtonText(dto.getButtonText());
        newSlider.setIsActive(dto.getIsActive());
        newSlider.setImageUrl(dto.getImageUrl());
        newSlider.setMobileImageUrl(dto.getMobileImageUrl());
        newSlider = sliderRepository.save(newSlider);
        return newSlider;
    }



    @Override
    public List<Slider> getSliderList() {
        return sliderRepository.findAll();
    }

    @Override
    public Page<Slider> getSliderListPaginated(String query, Pageable pageable) {
        return sliderRepository.findAllSliders(query, pageable);
    }

    @Override
    public Slider deleteSlider(Long id) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));

        sliderRepository.delete(slider);
        return slider;
    }
}
