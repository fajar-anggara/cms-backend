package com.backendapp.cms.security.service;

import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.dto.UserLoginRequest;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.users.repository.UserCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationOperationPerformer {
    private final AuthenticationManager authenticationManager;
    private final UserCrudRepository userRepository;
    private final JwtService jwtService;

    public AuthenticationOperationPerformer(AuthenticationManager authenticationManager,
                                            UserCrudRepository userRepository,
                                            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse authenticate(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifier(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getIdentifier())
                .orElseThrow(() -> new UsernameNotFoundException(request.getIdentifier()));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
