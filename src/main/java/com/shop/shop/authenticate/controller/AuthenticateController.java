package com.shop.shop.authenticate.controller;


import com.shop.shop.authenticate.dto.AuthResponse;
import com.shop.shop.authenticate.dto.LoginRequestDto;
import com.shop.shop.authenticate.service.AuthenticateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")

public class AuthenticateController {
    private final AuthenticateService authenticateService;

    public AuthenticateController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequestDto loginRequestDto) {
        System.out.println(loginRequestDto);
        return new ResponseEntity<>(authenticateService.login(loginRequestDto),HttpStatus.OK);
    }

}
