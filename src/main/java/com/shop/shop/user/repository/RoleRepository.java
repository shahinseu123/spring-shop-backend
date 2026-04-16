package com.shop.shop.user.repository;

import com.shop.shop.user.dto.role.RoleGetDto;
import com.shop.shop.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT id, name FROM roles", nativeQuery = true)
    List<RoleGetDto> findAllRoles();
}
