package com.nick.taskapp.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nick.taskapp.model.User;
import com.nick.taskapp.repository.UserRepository;
import com.nick.taskapp.security.JwtService;
/*
 * Handles authentication logic including user registration
 * and login operations.
 *
 * Responsible for password hashing, credential verification,
 * and JWT token generation.
 */
@Service
public class AuthService {
    /*
     * Repository for user database operations.
     */
    private final UserRepository userRepository;
    /*
     * Password encoder used for hashing and verifying passwords.
     */
    private final PasswordEncoder passwordEncoder;
    /*
     * Service used to generate JWT tokens.
     */
    private final JwtService jwtService;

    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /*
     * Registers a new user.
     *
     * Hashes the password before saving.
     */
    public void register(AuthRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    /*
     * Authenticates a user and returns a JWT token.
     *
     * Throws an exception if credentials are invalid.
     */
    public String login(AuthRequestDto request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> jwtService.generateToken(user.getUsername()))
                .orElse(null);
    }
}

/*
AuthService is the business logic for authentication.
It handles:
registering new users
logging users in
hashing passwords
verifying passwords
generating JWT tokens
This is the brain of your auth system.

1. Service layer pattern
Handles authentication logic separately from controller.
2. Password hashing
passwordEncoder.encode(...)
never stores raw passwords
stores hashed version instead
3. Password verification
passwordEncoder.matches(raw, hashed)
compares login password with stored hash
4. JWT token generation
jwtService.generateToken(user.getUsername())
creates token used for future authenticated requests
5. Repository usage
userRepository.findByUsername(...)
fetches user for login

AuthService is responsible for:
turning raw passwords into safe stored values
verifying credentials
generating tokens
*/