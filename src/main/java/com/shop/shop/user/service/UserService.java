package com.shop.shop.user.service;

import com.shop.shop.user.dto.role.RoleCreateDto;
import com.shop.shop.user.dto.user.UserCreateDto;
import com.shop.shop.user.dto.user.UserUpdateDto;
import com.shop.shop.user.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User getLoggedInUser();
    void create(UserCreateDto dto);
    void update(Long id, UserUpdateDto dto);
//    Boolean checkIfUserIsDeleted(String username);
    void createRole(RoleCreateDto dto);

}
