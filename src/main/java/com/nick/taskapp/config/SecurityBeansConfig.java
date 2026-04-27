package com.nick.taskapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/*
 * Defines security-related beans used across the application.
 *
 * Provides a PasswordEncoder for hashing and verifying passwords.
 */
@Configuration
public class SecurityBeansConfig {
    /*
     * Creates a BCrypt password encoder.
     *
     * Used by AuthService to hash passwords before storing them
     * and verify raw login passwords against stored hashes.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

/*
SecurityBeansConfig creates security-related beans that other parts of the app need.
Right now, it creates:
PasswordEncoder
That encoder is used by AuthService to hash passwords during registration and verify passwords during login.

1. Configuration class
@Configuration
Tells Spring this class defines beans.
2. Bean creation
@Bean
Tells Spring:
“Create this object and manage it for me.”
3. Password hashing
BCryptPasswordEncoder
BCrypt hashes passwords safely instead of storing raw passwords.
*/