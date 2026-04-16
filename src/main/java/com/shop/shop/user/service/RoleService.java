package com.shop.shop.user.service;

import com.shop.shop.user.dto.role.RoleGetDto;
import com.shop.shop.user.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findOneByName(String name);
    List<RoleGetDto> findAll();
}
