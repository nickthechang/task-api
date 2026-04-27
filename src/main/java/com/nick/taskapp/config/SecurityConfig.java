package com.nick.taskapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nick.taskapp.security.JwtAuthenticationFilter;
import com.nick.taskapp.security.JwtService;

/*
 * Configures application security rules.
 *
 * Defines which routes are public, which require authentication,
 * and registers the JWT filter used to authenticate requests.
 */
@Configuration
public class SecurityConfig {
    /*
     * Service used by the JWT filter to validate tokens.
     */
    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    /*
     * Creates the JWT authentication filter as a Spring bean.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }
    /*
     * Defines the security filter chain for HTTP requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                /*
                * JWT APIs should not rely on server-side sessions.
                */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/tasks/**").authenticated()
                        .anyRequest().permitAll()
                )

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

/*
SecurityConfig defines the security rules for your backend.
It answers:
which routes are public?
which routes require login?
where does the JWT filter run?
should CSRF be enabled or disabled?
This is the file that protects /tasks/**.

SecurityConfig decides which endpoints need authentication

1. Spring Security Configuration
Defines app-wide HTTP security behavior.
2. Security Filter Chain
SecurityFilterChain controls how requests pass through Spring Security.
3. Route Authorization
Uses route rules like:
.requestMatchers("/auth/**").permitAll()
.requestMatchers("/tasks/**").authenticated()
4. JWT Filter Registration
Adds your custom JWT filter before Spring’s username/password filter.
5. CSRF Disabled for Stateless API
For a REST API using JWT, CSRF is commonly disabled because authentication is handled through tokens, not server sessions.
*/