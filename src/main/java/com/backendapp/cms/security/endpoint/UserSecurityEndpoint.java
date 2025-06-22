package com.backendapp.cms.security.endpoint;

import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.security.service.UserRegistrationOperationPerformer;
import com.backendapp.cms.users.dto.UserEntityDto;
import com.backendapp.cms.users.endpoint.generated.UserControllerApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSecurityEndpoint implements UserControllerApi {

    public UserRegistrationOperationPerformer userRegistrationPerformer;

    public UserSecurityEndpoint (UserRegistrationOperationPerformer userRegistrationOperationPerformer) {
        this.userRegistrationPerformer = userRegistrationOperationPerformer;
    }
    @Override
    public ResponseEntity<UserRegister200Response> changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        return UserControllerApi.super.changePassword(userId, changePasswordRequest);
    }

    @Override
    public ResponseEntity<UserRegister200Response> userLogin(UserLoginRequest userLoginRequest) {
        return UserControllerApi.super.userLogin(userLoginRequest);
    }

    @Override
    public ResponseEntity<Success200Response> userLogout(Long userId) {
        return UserControllerApi.super.userLogout(userId);
    }

    @Override
    public ResponseEntity<UserRegister200Response> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        UserEntityDto register = userRegistrationPerformer.registerUser(request);
        return userRegistrationPerformer.getResponse(register);
    }

    @Override
    public ResponseEntity<UserRegister200Response> userRenewThePassword(Long userId, RenewPasswordRequest renewPasswordRequest) {
        return UserControllerApi.super.userRenewThePassword(userId, renewPasswordRequest);
    }
}