package com.backendapp.cms.users.endpoint;

import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.users.api.UserControllerApi;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.security.service.AuthenticationOperationPerformer;
import com.backendapp.cms.security.service.UserRegistrationOperationPerformer;
import com.backendapp.cms.users.converter.UserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.service.UserChangePasswordOperationPerformer;
import com.backendapp.cms.users.service.UserDeleteOperationPerformer;
import com.backendapp.cms.users.service.UserEntityProvider;
import com.backendapp.cms.users.service.UserUpdateOperationPerformer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserEndpoint implements UserControllerApi {
    private final UserRegistrationOperationPerformer userRegistrationOperationPerformer;
    private final UserConverter userConverter;
    private final JwtService jwtService;
    private final UserRegister200Response userSimpleResponse = new UserRegister200Response();
    private final AuthenticationOperationPerformer authenticationOperationPerformer;
    private final UserEntityProvider userEntityProvider;
    private final UserUpdateOperationPerformer userUpdateOperationPerformer;
    private final UserDeleteOperationPerformer userDeleteOperationPerformer;
    private final UserChangePasswordOperationPerformer userChangePasswordOperationPerformer;

    public UserEndpoint (
            UserRegistrationOperationPerformer userRegistrationOperationPerformer,
            UserConverter userConverter,
            JwtService jwtService,
            AuthenticationOperationPerformer authenticationOperationPerformer,
            UserEntityProvider userEntityProvider,
            UserUpdateOperationPerformer userUpdateOperationPerformer,
            UserDeleteOperationPerformer userDeleteOperationPerformer,
            UserChangePasswordOperationPerformer userChangePasswordOperationPerformer) {
        this.userRegistrationOperationPerformer = userRegistrationOperationPerformer;
        this.userConverter = userConverter;
        this.jwtService = jwtService;
        this.authenticationOperationPerformer = authenticationOperationPerformer;
        this.userEntityProvider = userEntityProvider;
        this.userUpdateOperationPerformer = userUpdateOperationPerformer;
        this.userDeleteOperationPerformer = userDeleteOperationPerformer;
        this.userChangePasswordOperationPerformer = userChangePasswordOperationPerformer;
    }

    @Override
    public ResponseEntity<UserRegister200Response> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        UserEntity register = userRegistrationOperationPerformer.registerUser(request);

        userSimpleResponse.setSuccess(Boolean.TRUE);
        userSimpleResponse.setMessage(register.getUsername() + " berhasil didaftarkan");
        userSimpleResponse.setData(userConverter.toSimpleResponse(register));
        userSimpleResponse.setToken(jwtService.generateToken(register));
        return ResponseEntity.ok(userSimpleResponse);
    }

    /**
     * Kita menggunakan userRegister200Response karena sama saja dto nya dengan userLogin200Response
     */
    @Override
    public ResponseEntity<UserRegister200Response> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        AuthenticationResponse token = authenticationOperationPerformer.authenticate(userLoginRequest);
        UserEntity user = userEntityProvider.getUser(userLoginRequest.getIdentifier());

        userSimpleResponse.setSuccess(Boolean.TRUE);
        userSimpleResponse.setMessage(user.getUsername() + " berhasil login");
        userSimpleResponse.setData(userConverter.toSimpleResponse(user));
        userSimpleResponse.setToken(token.getToken());
        return ResponseEntity.ok(userSimpleResponse);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUser200Response> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UserResponse userResponse = userConverter.toUserResponse(user);
        GetUser200Response response = new GetUser200Response();

        response.setSuccess(Boolean.TRUE);
        response.setMessage("Berhasil mendapatkan data dari user " + user.getUsername());
        response.setData(userResponse);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUser200Response> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity newUser = userUpdateOperationPerformer.updateUser(authentication, userUpdateRequest);
        UserResponse userResponse = userConverter.toUserResponse(newUser);
        GetUser200Response response = new GetUser200Response();

        response.setSuccess(Boolean.TRUE);
        response.setMessage("Berhasil mengupdate user " + newUser.getUsername());
        response.setData(userResponse);
        response.setToken(jwtService.generateToken(newUser));

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Success200Response> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userDeleteOperationPerformer.deleteUser(authentication);

        return ResponseEntity.ok(new Success200Response(true, "Berhasil menghapus user"));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserRenewThePassword200Response> changePassword(@Valid @RequestBody @NotNull ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userChangePasswordOperationPerformer.checkPassword(request, authentication);
        UserEntity userThatPasswordHasChange = userChangePasswordOperationPerformer.changePasswordUser(request, authentication);
        UserSimpleResponse userSimpleResponse = userConverter.toSimpleResponse(userThatPasswordHasChange);
        UserRenewThePassword200Response renewPasswordResponse = new UserRenewThePassword200Response();

        renewPasswordResponse.setSuccess(Boolean.TRUE);
        renewPasswordResponse.setMessage("Berhasil mengubah password user " + userThatPasswordHasChange.getUsername());
        renewPasswordResponse.setData(userSimpleResponse);

        return ResponseEntity.ok(renewPasswordResponse);
    }

    @Override
    public ResponseEntity<UserGetRefreshPasswordToken200Response> userGetRefreshPasswordToken() {
        // jadikan bisa di akses walau tanpa jwt bearer karena dia mungkin belum login dan lupa password
        // identifier menggunakan username atau email
    }
}
