package com.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authservice.jwt.JWTFilter;
import com.authservice.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JWTFilter jwtFilter;

    private String[] openUrl = {
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> {
            req.requestMatchers(openUrl).permitAll() // Public URLs
               .requestMatchers("/api/v1/welcome/message").hasRole("ADMIN") // Protect this URL for Admins
               .anyRequest().authenticated(); // All other requests require authentication
        }).authenticationProvider(authProvider())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        //.httpBasic(httpBasic -> {}); // Use the new `httpBasic()` approach

        return http.csrf(csrf -> csrf.disable()).build(); // Disable CSRF
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(getEncoder());
        return authProvider;
    }
}