package com.shop.shop.user.service;

import com.shop.shop.user.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public Optional<Role> findOneByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Role> findAll() {
        return List.of();
    }
}
