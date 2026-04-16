package com.shop.shop.user.dto.user;

import com.shop.shop.user.dto.role.RoleGetDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)

public class UserGetDto extends UserBaseDto {
    private Long id;
    private RoleGetDto role;

    public UserGetDto(Long id, RoleGetDto role,  String name, String email) {
        this.id = id;
        this.role = role;
        this.setEmail(email);
        this.setName(name);
    }
}
