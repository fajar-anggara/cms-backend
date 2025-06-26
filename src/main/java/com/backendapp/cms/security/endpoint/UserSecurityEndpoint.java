package com.backendapp.cms.security.endpoint;

import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.users.api.UserControllerApi;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.security.service.AuthenticationOperationPerformer;
import com.backendapp.cms.security.service.UserRegistrationOperationPerformer;
import com.backendapp.cms.users.converter.RegisterUserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.service.UserEntityProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSecurityEndpoint implements UserControllerApi {

    // Registration
    private final UserRegistrationOperationPerformer userRegistrationOperationPerformer;
    private final RegisterUserConverter registerUserConverter;
    private final JwtService jwtService;
    private final UserRegister200Response userSimpleResponse = new UserRegister200Response();
    private final AuthenticationOperationPerformer authenticationOperationPerformer;
    private final UserEntityProvider userEntityProvider;

    public UserSecurityEndpoint (
            UserRegistrationOperationPerformer userRegistrationOperationPerformer,
            RegisterUserConverter registerUserConverter,
            JwtService jwtService,
            AuthenticationOperationPerformer authenticationOperationPerformer,
            UserEntityProvider userEntityProvider) {
        this.userRegistrationOperationPerformer = userRegistrationOperationPerformer;
        this.registerUserConverter = registerUserConverter;
        this.jwtService = jwtService;
        this.authenticationOperationPerformer = authenticationOperationPerformer;
        this.userEntityProvider = userEntityProvider;
    }

    @Override
    public ResponseEntity<UserRegister200Response> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        UserEntity register = userRegistrationOperationPerformer.registerUser(request);

        userSimpleResponse.setSuccess(Boolean.TRUE);
        userSimpleResponse.setMessage(register.getUsername() + " berhasil didaftarkan");
        userSimpleResponse.setData(registerUserConverter.toSimpleResponse(register));
        userSimpleResponse.setToken(jwtService.generateToken(register));
        return ResponseEntity.ok(userSimpleResponse);
    }

    /**
     * Kita menggunakan userRegister200Response karena sama saja dto nya dengan userLogin200Response
     * @param userLoginRequest  (optional)
     * @return
     */
    @Override
    public ResponseEntity<UserRegister200Response> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        AuthenticationResponse token = authenticationOperationPerformer.authenticate(userLoginRequest);
        UserEntity user = userEntityProvider.getUser(userLoginRequest.getIdentifier());

        userSimpleResponse.setSuccess(Boolean.TRUE);
        userSimpleResponse.setMessage(user.getUsername() + " berhasil login");
        userSimpleResponse.setData(registerUserConverter.toSimpleResponse(user));
        userSimpleResponse.setToken(token.getToken());
        return ResponseEntity.ok(userSimpleResponse);
    }
}