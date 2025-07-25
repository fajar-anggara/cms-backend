package com.backendapp.cms.security.endpoint;

import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.users.api.SecurityControllerApi;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.entity.RefreshTokenEntity;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.security.repository.RefreshTokenRepository;
import com.backendapp.cms.security.service.*;
import com.backendapp.cms.users.converter.TokenResponseConverter;
import com.backendapp.cms.users.converter.UserRegisterConverter;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.dto.UserRegisterDto;
import com.backendapp.cms.users.entity.UserEntity;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
public class SecurityEndpoint implements SecurityControllerApi {
    private final UserRegistrationOperationPerformer userRegistrationOperationPerformer;
    private final UserResponseConverter userResponseConverter;
    private final TokenResponseConverter tokenResponseConverter;
    private final AuthenticationOperationPerformer authenticationOperationPerformer;
    private final UserEntityProvider userEntityProvider;
    private final UserRefreshPasswordOperationPerformer userRefreshPasswordOperationPerformer;
    private final JwtService jwtService;
    private final RefreshTokenOperationPerformer refreshTokenOperationPerformer;
    private final UserRegisterConverter userRegisterConverter;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public ResponseEntity<UserRegister200Response> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        UserRegisterDto userRegisterDto = userRegisterConverter.fromUserRegisterRequestToUserRegisterDto(request);
        UserEntity register = userRegistrationOperationPerformer.registerUser(userRegisterDto);

        UserRegister200Response userRegister200Response = new UserRegister200Response();
        userRegister200Response.setSuccess(Boolean.TRUE);
        userRegister200Response.setMessage(register.getUsername() + " berhasil didaftarkan");
        userRegister200Response.setData(userResponseConverter.fromUserEntityToSimpleResponse(register));
        userRegister200Response.setTokens(tokenResponseConverter.fromAuthenticationResponseToTokenResponse(jwtService.generateTokenAndRefreshToken(register)));

        return ResponseEntity.ok(userRegister200Response);
    }

    /**
     * Kita menggunakan userRegister200Response karena sama saja dto nya dengan userLogin200Response
     */
    @Override
    public ResponseEntity<UserRegister200Response> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        log.info("will call authenticationOperationPerformer, authenticate");
        AuthenticationResponse token = authenticationOperationPerformer.authenticate(userLoginRequest);
        UserEntity user = userEntityProvider.getUser(userLoginRequest.getIdentifier());
        log.info("Converted token, will send to api {}", tokenResponseConverter.fromAuthenticationResponseToTokenResponse(token));
        log.info("not duplicate yet - controller");
        UserRegister200Response userLogin200Response = new UserRegister200Response();
        userLogin200Response.setSuccess(Boolean.TRUE);
        userLogin200Response.setMessage(user.getUsername() + " berhasil login");
        userLogin200Response.setData(userResponseConverter.fromUserEntityToSimpleResponse(user));
        userLogin200Response.setTokens(tokenResponseConverter.fromAuthenticationResponseToTokenResponse(token));
        List<RefreshTokenEntity> listOfToken = refreshTokenRepository.findAll();
        log.info("will check database for token before return in login controller ");
        for (RefreshTokenEntity refreshTokenEntity : listOfToken) {
            log.info("Refresh token Id from database {}", refreshTokenEntity.getId());
        }


        return ResponseEntity.ok(userLogin200Response);
    }

    @Override
    public ResponseEntity<Success200Response> userGetRefreshPasswordToken(UserGetRefreshPasswordTokenRequest request) {
        String refreshPasswordToken = userRefreshPasswordOperationPerformer.getRefreshPasswordToken();
        userRefreshPasswordOperationPerformer.sendRefreshPasswordTokenTo(Objects.requireNonNull(request).getIdentifier(), refreshPasswordToken);

        return ResponseEntity.ok(new Success200Response(true, "Token refresh password berhasil dikirim, silahkan lihat email anda."));
    }

    @Override
    public ResponseEntity<UserRenewThePassword200Response> userRenewThePassword(@Valid @RequestBody RenewPasswordRequest renewPasswordRequest) {
        UserEntity updatedPasswordUser = userRefreshPasswordOperationPerformer.verifiedUserRefreshPassword(renewPasswordRequest);

        UserRenewThePassword200Response renewPasswordResponse = new UserRenewThePassword200Response();
        renewPasswordResponse.setSuccess(Boolean.TRUE);
        renewPasswordResponse.setMessage("Berhasil mengubah password user " + updatedPasswordUser.getUsername());
        renewPasswordResponse.setData(userResponseConverter.fromUserEntityToSimpleResponse(updatedPasswordUser));

        return ResponseEntity.ok(renewPasswordResponse);
    }

    @Override
    public ResponseEntity<TokenResponse> getRefreshToken(@Valid @RequestBody GetRefreshTokenRequest refreshTokenRequest) {
        UserEntity userThatTheTokenIsValidated = refreshTokenOperationPerformer.validateRefreshAndAccessToken(refreshTokenRequest.getAccessToken(), refreshTokenRequest.getRefreshToken());
        AuthenticationResponse generatedRefreshToken = jwtService.generateTokenAndRefreshToken(userThatTheTokenIsValidated);
        TokenResponseTokens convertedToken = tokenResponseConverter.fromAuthenticationResponseToTokenResponse(generatedRefreshToken);

        TokenResponse response = new TokenResponse();
        response.setTokens(convertedToken);
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Success200Response> userLogout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        refreshTokenOperationPerformer.deleteRefreshTokenByUser(user);
        return ResponseEntity.ok(new Success200Response(true, "Berhasil logout."));
    }


}
