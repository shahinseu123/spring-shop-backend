package com.shop.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationAttributeCreateDto {
    private String name;
    private String displayName;
    private String description;
    private String attributeType;
    private Boolean isActive;
    private Integer sortOrder;
    private List<VariationAttributeValueDto> values;
}
