package com.backendapp.cms.users.endpoint;


import com.backendapp.cms.openapi.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backendapp.cms.users.endpoint.generated.UserControllerApi;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@RequestMapping(UserEndpoint.API_MAIN_PATH)
public class UserEndpoint implements UserControllerApi {

    public static final String API_MAIN_PATH = "/api/v1/auth";

    @Override
    public ResponseEntity<UserRegister200Response> changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        return UserControllerApi.super.changePassword(userId, changePasswordRequest);
    }

    @Override
    public ResponseEntity<Success200Response> deleteUser(Long userId) {
        return UserControllerApi.super.deleteUser(userId);
    }

    @Override
    public ResponseEntity<GetUser200Response> getUser(Long userId) {
        return UserControllerApi.super.getUser(userId);
    }

    @Override
    public ResponseEntity<GetUser200Response> updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        return UserControllerApi.super.updateUser(userId, userUpdateRequest);
    }

    @Override
    public ResponseEntity<UserGetRefreshPasswordToken200Response> userGetRefreshPasswordToken(Long userId) {
        return UserControllerApi.super.userGetRefreshPasswordToken(userId);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return UserControllerApi.super.getRequest();
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
        return UserControllerApi.super.userRegister(request);
    }

    @Override
    public ResponseEntity<UserRegister200Response> userRenewThePassword(Long userId, RenewPasswordRequest renewPasswordRequest) {
        return UserControllerApi.super.userRenewThePassword(userId, renewPasswordRequest);
    }
}
