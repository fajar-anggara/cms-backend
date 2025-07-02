package com.backendapp.cms.security.service;

import com.backendapp.cms.common.constant.UserConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.security.validation.PasswordMatchValidator;
import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.security.validation.annotation.UniqueUser;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.security.repository.UserGrantedAuthorityRepository;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.dto.UserRegisterDto;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class UserRegistrationOperationPerformer {
    private final UserCrudRepository userCrudRepository;
    private final UserGrantedAuthorityRepository userGrantedAuthorityRepository;
    private final PasswordMatchValidator passwordMatchValidator;

    @Transactional
    public UserEntity registerUser(@Valid UserRegisterDto request) {

        log.info("encrypt password, and saved it.");
        String encryptedPassword = passwordMatchValidator.check(request.getPassword(), request.getConfirmPassword());
        UserGrantedAuthority defaultAuthority = setGrantedAuthority(UserConstants.DEFAULT_ROLE);

        UserEntity user = new UserEntity();
        request.getUsername().ifPresent(user::setUsername);
        request.getDisplayName().ifPresent(user::setDisplayName);
        request.getEmail().ifPresent(user::setEmail);
        user.setPassword(encryptedPassword);
        user.setProfilePicture(UserConstants.DEFAULT_PROFILE_PICTURE);
        user.setBio(UserConstants.DEFAULT_BIO);
        user.setAuthority(defaultAuthority);
        user.setEnabled(UserConstants.DEFAULT_ENABLE);
        user.setEmailVerified(UserConstants.DEFAULT_EMAIL_VERIFIED);

        return userCrudRepository.save(user);
    }

    public UserGrantedAuthority setGrantedAuthority(Authority authority) {
        return userGrantedAuthorityRepository.findByAuthority(authority)
                .orElseGet(() -> {
                    UserGrantedAuthority newAuthority = UserGrantedAuthority.builder()
                            .authority(authority)
                            .build();

                    return userGrantedAuthorityRepository.save(newAuthority);
                });
    }
}
