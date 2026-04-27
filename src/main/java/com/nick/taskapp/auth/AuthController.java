package com.nick.taskapp.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.nick.taskapp.dto.ApiResponse;
/*
 * REST controller for authentication endpoints.
 *
 * Handles user registration and login requests,
 * validates incoming auth DTOs, and delegates auth
 * logic to the service layer.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /*
     * Service containing authentication business logic.
     */
    private final AuthService authService;

    /*
     * Constructor injection allows Spring to provide AuthService.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
     * Registers a new user account.
     *
     * @Valid triggers validation rules in AuthRequestDto.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register(@Valid @RequestBody AuthRequestDto request) {
        authService.register(request);
        return ApiResponse.success("User registered", "OK");
    }

    /*
     * Authenticates a user and returns a JWT token.
     *
     * Returns 401 Unauthorized when credentials are invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody AuthRequestDto request) {
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

/*
AuthController is the HTTP/API layer for authentication.
It exposes:
POST /auth/register
POST /auth/login
Its job is to receive auth requests, validate the request DTO, call AuthService, and return an API response.

1. REST controller
@RestController
Makes this class handle HTTP requests and return JSON.
2. Base route
@RequestMapping("/auth")
All endpoints start with /auth.
3. Validation
@Valid @RequestBody AuthRequestDto request
Runs validation rules from AuthRequestDto before the service is called.
4. Response status
@ResponseStatus(HttpStatus.CREATED)
Makes register return 201 Created.
5. ResponseEntity
Used on login so you can return different HTTP statuses, like 200 OK or 401 Unauthorized.
*/