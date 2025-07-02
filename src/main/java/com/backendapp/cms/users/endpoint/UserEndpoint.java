package com.backendapp.cms.users.endpoint;

import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.users.api.UserControllerApi;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.users.converter.TokenResponseConverter;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.converter.UserUpdateConverter;
import com.backendapp.cms.users.dto.UserUpdateDto;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class UserEndpoint implements UserControllerApi{

    private final UserResponseConverter userResponseConverter;
    private final JwtService jwtService;
    private final UserUpdateOperationPerformer userUpdateOperationPerformer;
    private final UserDeleteOperationPerformer userDeleteOperationPerformer;
    private final UserChangePasswordOperationPerformer userChangePasswordOperationPerformer;
    private final TokenResponseConverter tokenResponseConverter;
    private final UserUpdateConverter userUpdateConverter;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUser200Response> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UserResponse userResponse = userResponseConverter.fromUseEntityToUserResponse(user);

        GetUser200Response response = new GetUser200Response();
        response.setSuccess(Boolean.TRUE);
        response.setMessage("Berhasil mendapatkan data dari user " + user.getUsername());
        response.setData(userResponse);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUser200Response> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserUpdateDto userUpdateDto = userUpdateConverter.fromUserUpdateRequestToUserUpdateDto(userUpdateRequest);
        UserEntity newUser = userUpdateOperationPerformer.updateUser(user, userUpdateDto);
        UserResponse userResponse = userResponseConverter.fromUseEntityToUserResponse(newUser);

        GetUser200Response response = new GetUser200Response();
        response.setSuccess(Boolean.TRUE);
        response.setMessage("Berhasil mengupdate user " + newUser.getUsername());
        response.setData(userResponse);
        response.setTokens(tokenResponseConverter.fromAuthenticationResponseToTokenResponse(jwtService.generateTokenAndRefreshToken(newUser)));

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
    public ResponseEntity<UserRenewThePassword200Response> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userChangePasswordOperationPerformer.checkPassword(request, authentication);
        UserEntity userThatPasswordHasChange = userChangePasswordOperationPerformer.changePasswordUser(request, authentication);
        UserSimpleResponse userSimpleResponse = userResponseConverter.fromUserEntityToSimpleResponse(userThatPasswordHasChange);

        UserRenewThePassword200Response renewPasswordResponse = new UserRenewThePassword200Response();
        renewPasswordResponse.setSuccess(Boolean.TRUE);
        renewPasswordResponse.setMessage("Berhasil mengubah password user " + userThatPasswordHasChange.getUsername());
        renewPasswordResponse.setData(userSimpleResponse);

        return ResponseEntity.ok(renewPasswordResponse);
    }

}
