// src/main/java/com/backendapp/cms/security/config/ApplicationConfig.java
package com.backendapp.cms.security.config;

import com.backendapp.cms.users.converter.RegisterUserConverter;
import com.backendapp.cms.users.repository.UserCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserCrudRepository userRepository; // Injeksikan UserRepository Anda

    // UserDetailsService: Memberitahu Spring bagaimana memuat detail pengguna dari DB
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username) // Asumsi ada findByUsername di UserRepository Anda
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // AuthenticationProvider: Mengelola proses autentikasi (mengambil user details, mencocokkan password)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Set UserDetailsService kita
        authProvider.setPasswordEncoder(passwordEncoder()); // Set PasswordEncoder kita
        return authProvider;
    }

    // AuthenticationManager: Orchestrates the authentication process
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // PasswordEncoder: Untuk mengenkripsi dan memverifikasi password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}