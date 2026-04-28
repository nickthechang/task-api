package com.nick.taskapp.config;

import com.nick.taskapp.security.JwtAuthenticationFilter;
import com.nick.taskapp.security.JwtService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
/*
 * Configures application security rules.
 *
 * Defines which routes are public, which require authentication,
 * and registers the JWT filter used to authenticate requests.
 */
@Configuration
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * Enable CORS inside Spring Security.
                 */
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                /*
                 * Disable CSRF for stateless JWT API.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * Do not use server-side sessions.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        /*
                         * Allow browser preflight requests.
                         */
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        /*
                         * Auth endpoints are public.
                         */
                        .requestMatchers("/auth/**").permitAll()

                        /*
                         * Task endpoints require JWT authentication.
                         */
                        .requestMatchers("/tasks/**").authenticated()

                        .anyRequest().permitAll()
                )

                /*
                 * Run JWT filter before Spring Security's default auth filter.
                 */
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * Defines CORS rules used by Spring Security.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
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