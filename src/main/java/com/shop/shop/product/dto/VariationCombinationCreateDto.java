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
public class VariationCombinationCreateDto {
    private Long productId;
    private Map<String, String> combination; // e.g., {"Color": "Red", "Size": "Large"}
    private Integer quantity;
    private BigDecimal price;
    private String sku;
}
