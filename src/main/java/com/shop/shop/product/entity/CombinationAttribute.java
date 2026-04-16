package com.shop.shop.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "combination_attributes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CombinationAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combination_id")
    private VariationCombination combination;

    @Column(nullable = false)
    private String attributeKey; // e.g., "Color"

    @Column(nullable = false)
    private String attributeValue; // e.g., "Red"
}
