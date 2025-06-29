package com.backendapp.cms.users.endpoint;

import com.backendapp.cms.email.service.EmailService;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.users.api.UserControllerApi;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.security.service.AuthenticationOperationPerformer;
import com.backendapp.cms.security.service.UserRegistrationOperationPerformer;
import com.backendapp.cms.users.converter.UserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
public class UserEndpoint implements UserControllerApi {
    private final UserRegistrationOperationPerformer userRegistrationOperationPerformer;
    private final UserConverter userConverter;
    private final JwtService jwtService;
    private final AuthenticationOperationPerformer authenticationOperationPerformer;
    private final UserEntityProvider userEntityProvider;
    private final UserUpdateOperationPerformer userUpdateOperationPerformer;
    private final UserDeleteOperationPerformer userDeleteOperationPerformer;
    private final UserChangePasswordOperationPerformer userChangePasswordOperationPerformer;
    private final UserRefreshPasswordOperationPerformer userRefreshPasswordOperationPerformer;

    @Override
    public ResponseEntity<UserRegister200Response> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        UserEntity register = userRegistrationOperationPerformer.registerUser(request);
        UserRegister200Response userRegister200Response = new UserRegister200Response();
        userRegister200Response.setSuccess(Boolean.TRUE);
        userRegister200Response.setMessage(register.getUsername() + " berhasil didaftarkan");
        userRegister200Response.setData(userConverter.toSimpleResponse(register));
        userRegister200Response.setToken(jwtService.generateToken(register));

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
        userLogin200Response.setToken(token.getToken());

        return ResponseEntity.ok(userLogin200Response);
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
    public ResponseEntity<ChangePassword200Response> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userChangePasswordOperationPerformer.checkPassword(request, authentication);
        UserEntity userThatPasswordHasChange = userChangePasswordOperationPerformer.changePasswordUser(request, authentication);
        UserSimpleResponse userSimpleResponse = userConverter.toSimpleResponse(userThatPasswordHasChange);

        ChangePassword200Response renewPasswordResponse = new ChangePassword200Response();
        renewPasswordResponse.setSuccess(Boolean.TRUE);
        renewPasswordResponse.setMessage("Berhasil mengubah password user " + userThatPasswordHasChange.getUsername());
        renewPasswordResponse.setData(userSimpleResponse);

        return ResponseEntity.ok(renewPasswordResponse);
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
        String token = jwtService.generateToken(updatedPasswordUser);

        UserRenewThePassword200Response renewPasswordResponse = new UserRenewThePassword200Response();
        renewPasswordResponse.setSuccess(Boolean.TRUE);
        renewPasswordResponse.setMessage("Berhasil mengubah password user " + updatedPasswordUser.getUsername());
        renewPasswordResponse.setData(userConverter.toSimpleResponse(updatedPasswordUser));
        renewPasswordResponse.setToken(token);

        return ResponseEntity.ok(renewPasswordResponse);
    }


}
