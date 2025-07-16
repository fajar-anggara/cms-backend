package com.backendapp.cms.security.service;

import com.backendapp.cms.openapi.dto.UserLoginRequest;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AuthenticationOperationPerformer {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse authenticate(UserLoginRequest request) {
        log.info("AuthenticatingOperationPerformer called");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifier(),
                        request.getPassword()
                )
        );

        AuthenticationResponse tokens = jwtService.generateTokenAndRefreshToken((UserEntity) authentication.getPrincipal());
        AuthenticationResponse response = AuthenticationResponse.builder().accessToken(tokens.getAccessToken()).refreshToken(tokens.getRefreshToken()).build();

        return response;
    }
}
