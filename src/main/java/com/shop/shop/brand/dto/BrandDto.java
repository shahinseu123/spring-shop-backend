package com.shop.shop.brand.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandDto {
  private Long id;
  private String name;
  private String description;
  private String logo;
  private String website;
  private String email;
  private String phone;
  private Boolean isActive;
  private LocalDateTime createdAt;
}
