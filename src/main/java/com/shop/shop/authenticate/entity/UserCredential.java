package com.shop.shop.authenticate.entity;

import com.shop.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String clientSecret;

    @OneToOne
    private User user;
}
