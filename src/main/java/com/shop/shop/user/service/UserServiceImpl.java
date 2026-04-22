package com.shop.shop.user.service;

import com.shop.shop.authenticate.entity.UserCredential;
import com.shop.shop.authenticate.repository.UserCredentialRepository;
import com.shop.shop.user.dto.role.RoleCreateDto;
import com.shop.shop.user.dto.user.UserCreateDto;
import com.shop.shop.user.dto.user.UserUpdateDto;
import com.shop.shop.user.entity.Role;
import com.shop.shop.user.entity.User;
import com.shop.shop.user.repository.RoleRepository;
import com.shop.shop.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final String USER_MODULE = "USERS";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialRepository userCredentialRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserCredentialRepository userCredentialRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // username property from authentication is actually email property
        // for system user
        Optional<User> user = userRepository.findByEmail(username);
        return user.orElse(null);
    }

    @Override
    public void create(UserCreateDto dto) {
        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        if(dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
            if (role == null) throw new RuntimeException("No Role found for this user");
            newUser.setRole(role);
        }

        newUser = userRepository.save(newUser);

        UserCredential newCred = new UserCredential();
        newCred.setUsername(dto.getEmail());
        newCred.setPassword(passwordEncoder.encode(dto.getPassword()));
        newCred.setUser(newUser);
        userCredentialRepository.save(newCred);

    }

    @Override
    public void update(Long id, UserUpdateDto dto) {

    }

    @Override
    public Boolean checkIfUserIsDeleted(String username) {
        Optional<User> userOp = userRepository.findByEmail(username);
        if(userOp.isEmpty()) throw new RuntimeException("No user found with this email");
        return userOp.get().getDeleteStatus().equals(DeleteStatus.YES);
    }

    @Override
    public void createRole(RoleCreateDto dto) {
        Role newRole = new Role();
        newRole.setName(dto.getName());
        roleRepository.save(newRole);
    }
}
