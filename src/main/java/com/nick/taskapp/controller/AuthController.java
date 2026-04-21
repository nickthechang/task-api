package com.nick.taskapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.nick.taskapp.dto.ApiResponse;
import com.nick.taskapp.dto.AuthRequestDto;
import com.nick.taskapp.dto.AuthResponseDto;
import com.nick.taskapp.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register(@RequestBody AuthRequestDto request) {
        authService.register(request);
        return ApiResponse.success("User registered", "OK");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody AuthRequestDto request) {
        String token = authService.login(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid username or password"));
        }

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", new AuthResponseDto(token))
        );
    }
}