package com.shop.shop.user.service;

import com.shop.shop.user.dto.role.RoleGetDto;
import com.shop.shop.user.entity.Role;
import com.shop.shop.user.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findOneByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<RoleGetDto> findAll() {
        return  roleRepository.findAllRoles();
    }
}
