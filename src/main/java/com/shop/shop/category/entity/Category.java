package com.shop.shop.category.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.shop.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug; // URL-friendly identifier e.g. "mens-shoes"

    private String imageUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    // Self-referencing for nested/sub-categories
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Category> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Helper: check if this is a root-level category
    @Transient
    public boolean isRootCategory() {
        return parent == null;
    }

    // Helper: add sub-category with bidirectional sync
    public void addSubCategory(Category child) {
        subCategories.add(child);
        child.setParent(this);
    }

    public void removeSubCategory(Category child) {
        subCategories.remove(child);
        child.setParent(null);
    }



}
