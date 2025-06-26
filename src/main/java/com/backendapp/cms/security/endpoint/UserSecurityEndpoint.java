package com.backendapp.cms.security.endpoint;

import com.backendapp.cms.common.constant.ApiConstants;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.users.api.UserControllerApi;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.security.service.UserRegistrationOperationPerformer;
import com.backendapp.cms.users.converter.RegisterUserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSecurityEndpoint implements UserControllerApi {

    // Registration
    private final UserRegistrationOperationPerformer userRegistrationOperationPerformer;
    private final RegisterUserConverter registerUserConverter;
    private final JwtService jwtService;

    public UserSecurityEndpoint (
            UserRegistrationOperationPerformer userRegistrationOperationPerformer,
            RegisterUserConverter registerUserConverter,
            JwtService jwtService
    ) {
        this.userRegistrationOperationPerformer = userRegistrationOperationPerformer;
        this.registerUserConverter = registerUserConverter;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<UserRegister200Response> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        UserEntity register = userRegistrationOperationPerformer.registerUser(request);

        UserRegister200Response registeredUser = new UserRegister200Response();
        registeredUser.setSuccess(Boolean.TRUE);
        registeredUser.setMessage(register.getUsername() + " berhasil didaftarkan");
        registeredUser.setData(registerUserConverter.toSimpleResponse(register));
        registeredUser.setToken(jwtService.generateToken(register));
        return ResponseEntity.ok(registeredUser);
    }

}