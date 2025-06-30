package com.backendapp.cms.users.converter;

import com.backendapp.cms.openapi.dto.TokenResponse;
import com.backendapp.cms.openapi.dto.TokenResponseTokens;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenResponseConverter {

    AuthenticationResponse toAuthenticationResponse(TokenResponse tokenResponse);
    TokenResponseTokens toTokenResponse(AuthenticationResponse authenticationResponse);
}
