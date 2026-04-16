package com.shop.shop.authenticate.repository;

import com.shop.shop.authenticate.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByUsernameAndPassword(String username, String password);

    Optional<UserCredential> findByUsername(String username);

    Optional<UserCredential> findByClientSecret(String secret);
    @Query("SELECT uc from UserCredential uc WHERE uc.user.id = :userId")
    Optional<UserCredential> findByUserId(@Param("userId")Long userId);
}
