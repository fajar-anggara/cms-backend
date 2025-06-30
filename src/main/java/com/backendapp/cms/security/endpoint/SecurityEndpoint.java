package com.backendapp.cms.security.endpoint;

import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.users.api.SecurityControllerApi;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.security.service.*;
import com.backendapp.cms.users.converter.TokenResponseConverter;
import com.backendapp.cms.users.converter.UserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
public class SecurityEndpoint implements SecurityControllerApi {
    private final UserRegistrationOperationPerformer userRegistrationOperationPerformer;
    private final UserConverter userConverter;
    private final TokenResponseConverter tokenResponseConverter;
    private final AuthenticationOperationPerformer authenticationOperationPerformer;
    private final UserEntityProvider userEntityProvider;
    private final UserRefreshPasswordOperationPerformer userRefreshPasswordOperationPerformer;
    private final JwtService jwtService;
    private final RefreshTokenOperationPerformer refreshTokenOperationPerformer;

    @Override
    public ResponseEntity<UserRegister200Response> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        UserEntity register = userRegistrationOperationPerformer.registerUser(request);

        UserRegister200Response userRegister200Response = new UserRegister200Response();
        userRegister200Response.setSuccess(Boolean.TRUE);
        userRegister200Response.setMessage(register.getUsername() + " berhasil didaftarkan");
        userRegister200Response.setData(userConverter.toSimpleResponse(register));
        userRegister200Response.setTokens(tokenResponseConverter.toTokenResponse(jwtService.generateTokenAndRefreshToken(register)));

        return ResponseEntity.ok(userRegister200Response);
    }

    /**
     * Kita menggunakan userRegister200Response karena sama saja dto nya dengan userLogin200Response
     */
    @Override
    public ResponseEntity<UserRegister200Response> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        AuthenticationResponse token = authenticationOperationPerformer.authenticate(userLoginRequest);
        UserEntity user = userEntityProvider.getUser(userLoginRequest.getIdentifier());

        UserRegister200Response userLogin200Response = new UserRegister200Response();
        userLogin200Response.setSuccess(Boolean.TRUE);
        userLogin200Response.setMessage(user.getUsername() + " berhasil login");
        userLogin200Response.setData(userConverter.toSimpleResponse(user));
        userLogin200Response.setTokens(tokenResponseConverter.toTokenResponse(token));

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
        renewPasswordResponse.setData(userConverter.toSimpleResponse(updatedPasswordUser));

        return ResponseEntity.ok(renewPasswordResponse);
    }

    @Override
    public ResponseEntity<TokenResponse> getRefreshToken(@Valid @RequestBody GetRefreshTokenRequest refreshTokenRequest) {
        UserEntity userThatTheTokenIsValidated = refreshTokenOperationPerformer.validateRefreshAndAccessToken(refreshTokenRequest.getAccessToken(), refreshTokenRequest.getRefreshToken());
        TokenResponse response = new TokenResponse();
        response.setTokens(tokenResponseConverter.toTokenResponse(jwtService.generateTokenAndRefreshToken(userThatTheTokenIsValidated)));
        return ResponseEntity.ok(response);
    }
}
