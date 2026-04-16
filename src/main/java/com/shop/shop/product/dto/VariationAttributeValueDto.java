package com.shop.shop.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationAttributeValueDto {
    private Long id;
    private Long attributeId;
    private String value;
    private String displayValue;
    private String colorCode;
    private String imageUrl;
    private Integer sortOrder;
}
