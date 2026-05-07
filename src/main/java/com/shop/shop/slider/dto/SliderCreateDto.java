package com.shop.shop.slider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SliderCreateDto {
    private Long id;
    private String title;
    private String subtitle;
    private String description;
    private String imageUrl;
    private String mobileImageUrl;
    private String buttonText;
    private Boolean isActive;
}
