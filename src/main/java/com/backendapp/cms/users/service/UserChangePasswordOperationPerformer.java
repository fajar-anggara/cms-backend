package com.backendapp.cms.users.service;

import com.backendapp.cms.common.exception.ResourceNotFoundException;
import com.backendapp.cms.openapi.dto.ChangePasswordRequest;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserChangePasswordOperationPerformer {
    private final UserCrudRepository userCrudRepository;
    private final PasswordEncoder passwordEncoder;

    public UserChangePasswordOperationPerformer(
            UserCrudRepository userCrudRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userCrudRepository = userCrudRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void checkPassword(ChangePasswordRequest request, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String oldPassword = passwordEncoder.encode(request.getOldPassword());
        if(!oldPassword.equals(user.getPassword()) || !request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new PasswordMismatchException();
        }
    }

    public UserEntity changePasswordUser(@Valid ChangePasswordRequest request, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UserEntity userThatPasswordWillBeChange = userCrudRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        userThatPasswordWillBeChange.setPassword(newPassword);
        return userCrudRepository.save(userThatPasswordWillBeChange);
    }
}
