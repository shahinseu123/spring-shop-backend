package com.shop.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationCombinationGetDto {
    private Long id;
    private Map<String, String> combination;
    private String combinationHash;
    private Integer quantity;
    private BigDecimal price;
    private String sku;
    private Long variationId; // If linked to a variation
}
