package com.shop.shop.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitDto {
    private Long id;
    private String name;
    private String abbreviation;
    private String description;
    private Boolean isActive;
}
