package com.backendapp.cms.security.config;

import com.backendapp.cms.common.constant.ApiConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPointHandler jwtAuthenticationEntryPointHandler;
    private final DeniedHandler deniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(ApiConstants.SUPERUSER_PATH).hasRole(String.valueOf(Authority.ADMIN))
                        .requestMatchers(ApiConstants.AUTHOR_PATH).hasAnyRole(String.valueOf(Authority.BLOGGER), String.valueOf(Authority.ADMIN))
                        .requestMatchers(ApiConstants.USER_PATH).hasAnyRole(String.valueOf(Authority.BLOGGER), String.valueOf(Authority.ADMIN))
                        .requestMatchers(ApiConstants.AUTH_PATH).permitAll()
                        .requestMatchers((ApiConstants.PUBLIC_PATH)).permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-ui.html/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPointHandler)
                        .accessDeniedHandler(deniedHandler)
                );

        return http.build();
    }
}