package com.backendapp.cms.security.config;

import com.backendapp.cms.common.constant.ApiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(ApiConstants.SUPERUSER_PATH+ "/**").hasRole("SUPERUSER")
                        .requestMatchers(ApiConstants.USER_PATH+ "/**").hasRole("USER")
                        .requestMatchers(ApiConstants.AUTHOR_PATH + "/**").hasRole("USER")
                        .requestMatchers(ApiConstants.AUTH_PATH + "/**").permitAll()
                        .requestMatchers(ApiConstants.PUBLIC_PATH + "/**").permitAll()
                        .anyRequest().authenticated()).httpBasic(Customizer.withDefaults());

        return http.build();

    }
}