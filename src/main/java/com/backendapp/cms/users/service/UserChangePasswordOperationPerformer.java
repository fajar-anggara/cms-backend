package com.backendapp.cms.users.service;

import com.backendapp.cms.common.exception.ResourceNotFoundException;
import com.backendapp.cms.openapi.dto.ChangePasswordRequest;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameNotFoundException;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserChangePasswordOperationPerformer {
    private final UserCrudRepository userCrudRepository;
    private final PasswordEncoder passwordEncoder;

    public void checkPassword(ChangePasswordRequest request, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordMismatchException();
        }
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new PasswordMismatchException();
        }
    }

    public UserEntity changePasswordUser(ChangePasswordRequest request, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UserEntity userThatPasswordWillBeChange = userCrudRepository.findByUsernameAndDeletedAtIsNull(user.getUsername())
                .orElseThrow(UsernameOrEmailNotFoundException::new);
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        userThatPasswordWillBeChange.setPassword(newPassword);
        return userCrudRepository.save(userThatPasswordWillBeChange);
    }
}
