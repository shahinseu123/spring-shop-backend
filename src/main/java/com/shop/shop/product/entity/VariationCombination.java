package com.shop.shop.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "variation_combinations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationCombination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "variation_id")
    private ProductVariation variation;

    private String combinationHash;
    private Integer quantity;
    private BigDecimal price;
    private String sku;

    // One-to-many relationship for attributes
    @OneToMany(mappedBy = "combination", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CombinationAttribute> attributes = new ArrayList<>();

    public void addAttribute(String key, String value) {
        CombinationAttribute attribute = new CombinationAttribute();
        attribute.setAttributeKey(key);
        attribute.setAttributeValue(value);
        attribute.setCombination(this);
        attributes.add(attribute);
    }
}


