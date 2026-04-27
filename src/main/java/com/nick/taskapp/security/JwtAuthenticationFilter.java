package com.nick.taskapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
/*
 * JWT authentication filter that runs on every request.
 *
 * Extracts the JWT from the Authorization header,
 * validates it, and sets the authenticated user in
 * the Spring Security context.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    /*
     * Checks each incoming request for a valid JWT.
     *
     * If no token is provided, the request continues unauthenticated.
     * SecurityConfig decides whether unauthenticated access is allowed.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        /*
         * Read the Authorization header.
         * Expected format: Authorization: Bearer <token>
         */                               
        String authHeader = request.getHeader("Authorization");
        /*
         * If the header is missing or not a Bearer token,
         * continue without setting authentication.
         */                             
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        /*
         * Remove "Bearer " and keep only the JWT value.
         */
        String token = authHeader.substring(7);
        
        try {
            /*
             * Extract and verify the username from the token.
             */
            String username = jwtService.extractUsername(token);
            /*
             * If the token is valid and no user is authenticated yet,
             * create an authentication object for this request.
             */
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            /*
             * Invalid or expired tokens receive a 401 response immediately.
             */
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                {"success":false,"data":null,"message":"Invalid or expired token"}
                """);
            return;
        }
        /*
         * Continue the filter chain after authentication is handled.
         */
        filterChain.doFilter(request, response);
    }
}

/*
This is the gatekeeper of your backend.
JwtAuthenticationFilter runs on every incoming request and checks:
“Does this request have a valid JWT token?”
If yes → allow access
If no → block or treat as unauthenticated

1. Filter (OncePerRequestFilter)
extends OncePerRequestFilter
Means:
runs once for every HTTP request
2. Authorization header parsing
Authorization: Bearer <token>
Extracts the token from the request.
3. JWT validation
Uses:
jwtService.extractUsername(token)
to read the token and verify it.
4. SecurityContext
SecurityContextHolder
Stores authenticated user info for the rest of the request.
*/