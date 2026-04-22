package com.shop.shop.user.entity;


import com.shop.shop.user.service.DeleteStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
//    @Enumerated(EnumType.STRING)
//    private DeleteStatus deleteStatus = DeleteStatus.NO;
    @ManyToOne
    private Role role;
    public User(Long id) {
        this.id = id;
    }

}
