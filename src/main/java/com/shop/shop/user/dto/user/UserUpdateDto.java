package com.shop.shop.user.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserUpdateDto extends UserBaseDto{
    private Long roleId;
    private String username;
}
