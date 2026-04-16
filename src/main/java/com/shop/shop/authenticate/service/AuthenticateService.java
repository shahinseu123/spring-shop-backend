package com.shop.shop.authenticate.service;

import com.shop.shop.authenticate.dto.AuthResponse;
import com.shop.shop.authenticate.dto.LoginRequestDto;
import com.shop.shop.authenticate.entity.UserCredential;
import com.shop.shop.authenticate.repository.UserCredentialRepository;
import com.shop.shop.authenticate.util.JwtUtil;
import com.shop.shop.user.entity.Role;
import com.shop.shop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AuthenticateService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Lazy
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> userCredOp = userCredentialRepository.findByUsername(username);
        if(userCredOp.isEmpty()){
            throw new RuntimeException("Sorry! username not valid");
        }
        UserCredential userCredential = userCredOp.get();
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(userCredential.getUser().getRole().getName())
        );
        return new User(userCredential.getUsername(),userCredential.getPassword(), authorities);
    }

    public UserDetails loadUserByClientSecret(String secret){
        Optional<UserCredential> userCredOp = userCredentialRepository.findByClientSecret(secret);
        if(userCredOp.isEmpty()){
            throw new RuntimeException("Sorry! username not valid");
        }
        UserCredential userCredential = userCredOp.get();
        return new User(userCredential.getUsername(),userCredential.getPassword(),new ArrayList<>());
    }

    public AuthResponse login(LoginRequestDto loginRequestDto){
        UserDetails userDetails =  loadUserByUsername(loginRequestDto.getUsername());
        if(!passwordEncoder.matches(loginRequestDto.getPassword(),userDetails.getPassword())){
            throw new RuntimeException("Sorry! Username or Password Invalid");
        }
        if(userService.checkIfUserIsDeleted(loginRequestDto.getUsername())) throw new RuntimeException("Sorry! This User has been deleted");
        AuthResponse authResponse = new AuthResponse();
        String token;
        Optional<UserCredential> userCredOp = userCredentialRepository.findByUsername(userDetails.getUsername());
        if(userCredOp.isPresent()){
            UserCredential uc = userCredOp.get();
            String email = userDetails.getUsername();
            Role role = uc.getUser().getRole();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(uc.getUsername(),null,new ArrayList<>()));
            token = jwtUtil.generateToken(email, role.getName());
            authResponse.setToken(token);
            authResponse.setRoles(List.of(role.getName()));
        }
        return authResponse;
    }

}