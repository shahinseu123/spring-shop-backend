package com.shop.shop.user.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class RoleGetDto extends RoleBaseDto{
    private Long id;

    public RoleGetDto(Long id, String name) {
        this.id = id;
        this.setName(name);
    }
}
