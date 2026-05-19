package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Désactive CSRF (nécessaire pour tester les APIs REST de l'extérieur)
            .csrf(csrf -> csrf.disable())
            
            // Configuration des autorisations d'accès
            .authorizeHttpRequests(auth -> auth
                // 1. On autorise l'accès public aux endpoints d'authentification
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // 2. On autorise TOUTES les ressources nécessaires à Swagger UI
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                
                // 3. Tout le reste de l'application requiert une authentification
                .anyRequest().authenticated()
            );
            
        return http.build();
    }
}