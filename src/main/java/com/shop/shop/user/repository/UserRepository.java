package com.shop.shop.user.repository;

import com.shop.shop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("    SELECT u from User u " + "   WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
