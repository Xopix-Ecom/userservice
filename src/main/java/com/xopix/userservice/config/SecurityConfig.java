package com.xopix.userservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for API-only service
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/**").permitAll() // Allow public registration
                        .requestMatchers("/api/users/_internal/**").permitAll() // Allow internal endpoint access (secure via network policies in prod)
                        .anyRequest().authenticated() // All other requests need authentication (handled by API Gateway)
                );
        // Note: Actual JWT validation for authenticated endpoints happens at Kong API Gateway.
        // The UserService simply trusts the Auth0-validated headers/context from Kong.
        // For local testing without Kong, you might add JWT filter here.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
