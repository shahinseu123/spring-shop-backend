package com.shop.shop.user.dto.user;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreateDto extends UserBaseDto {
    private String username;
    private String password;
    private Long roleId;
}

