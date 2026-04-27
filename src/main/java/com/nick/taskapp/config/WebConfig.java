package com.nick.taskapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
 * Configures CORS settings for the application.
 *
 * Allows the frontend application to make requests
 * to this backend from a different origin.
 */
@Configuration
public class WebConfig {
    /*
     * Defines global CORS rules.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        /*
                        * Allow requests from Angular frontend.
                        */        
                        .allowedOrigins("http://localhost:4200")
                        /*
                        * Allow common HTTP methods used by the API.
                        */
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        /*
                        * Allow all headers (including Authorization for JWT).
                        */
                        .allowedHeaders("*");
            }
        };
    }
}

/*
WebConfig controls CORS (Cross-Origin Resource Sharing).
It answers:
“Is my frontend allowed to call my backend?”

Your frontend (Angular on port 4200) is a different origin than your backend (Spring Boot on 8080).
By default, browsers block this.
This file says:
“Yeah, http://localhost:4200 is allowed to talk to me.”

1. Web MVC configuration
implements WebMvcConfigurer
Allows customizing web behavior like CORS.
2. Global CORS mapping
addCorsMappings(...)
Applies rules across the entire app.
3. Allowed origins
.allowedOrigins("http://localhost:4200")
Defines which frontend is allowed.
4. Allowed methods
.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
Defines which HTTP methods are allowed.
*/