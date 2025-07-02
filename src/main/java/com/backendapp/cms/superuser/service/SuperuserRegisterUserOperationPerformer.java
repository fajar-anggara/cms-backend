package com.backendapp.cms.superuser.service;

import com.backendapp.cms.common.constant.UserConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.CreateUserRequest;
import com.backendapp.cms.security.validation.PasswordMatchValidator;
import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.security.validation.annotation.UniqueUser;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.security.repository.UserGrantedAuthorityRepository;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SuperuserRegisterUserOperationPerformer {

    private final UserCrudRepository userCrudRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGrantedAuthorityRepository userGrantedAuthorityRepository;

    public UserEntity registerUser(
            @Valid
            @UniqueUser
            @UniqueEmail
            CreateUserRequest request) {

        log.info("encrypt password, and saved it. -- this is superuser");
        String matchPassword = PasswordMatchValidator.check(request.getPassword(), request.getConfirmPassword());
        String encryptedPassword = passwordEncoder.encode(matchPassword);

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .displayName(request.getDisplayName())
                .email(request.getEmail())
                .enabled(isEnabled(request.getEnabled()))
                .isEmailVerified(isEmailVerified(request.getIsEmailVerified()))
                .authority(isAuthority(request.getAuthority()))
                .password(encryptedPassword)
                .build();

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

    private UserGrantedAuthority isAuthority(CreateUserRequest.AuthorityEnum authority) {
        if (authority != null) {
            Authority requestAuthority = Authority.valueOf(authority.toString());
            return userGrantedAuthorityRepository.findByAuthority(requestAuthority)
                    .orElseGet(() -> {
                        UserGrantedAuthority newAuthority = UserGrantedAuthority.builder()
                                .authority(requestAuthority)
                                .build();

                        return userGrantedAuthorityRepository.save(newAuthority);
                    });
        } else {
            return setGrantedAuthority(Authority.BLOGGER);
        }
    }

    private boolean isEnabled(Boolean enabled) {
        if (enabled != null) {
            return enabled;
        } else {
            return UserConstants.DEFAULT_ENABLE;
        }
    }

    private boolean isEmailVerified(Boolean emailVerified) {
        if(emailVerified != null) {
            return emailVerified;
        } else {
            return UserConstants.DEFAULT_EMAIL_VERIFIED;
        }
    }

}
