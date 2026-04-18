package com.shop.shop.brand.dto;

import com.shop.shop.IsActive;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandDto {
  private Long id;
  private String name;
  private String slug;
  private String logoUrl;
  @Enumerated(value = EnumType.STRING)
  private IsActive active;
}
