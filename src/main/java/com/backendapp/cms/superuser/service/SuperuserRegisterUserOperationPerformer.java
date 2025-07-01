package com.backendapp.cms.superuser.service;

import com.backendapp.cms.common.constant.UserConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.CreateUserRequest;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.security.exception.EmailAlreadyExistException;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.security.repository.UserGrantedAuthorityRepository;
import com.backendapp.cms.security.service.UserRegistrationOperationPerformer;
import com.backendapp.cms.users.converter.UserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameAlreadyExistException;
import com.backendapp.cms.users.exception.UsernameOrEmailUsedToExistException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SuperuserRegisterUserOperationPerformer {

    private final UserCrudRepository userCrudRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGrantedAuthorityRepository userGrantedAuthorityRepository;

    public UserEntity registerUser(@Valid CreateUserRequest request) {
        log.info("Validating register request, then setting authority, encrypt password, and saved it. -- this is superuser");
        if (userCrudRepository.existsByUsernameAndDeletedAtIsNull(request.getUsername())) {
            throw new UsernameAlreadyExistException();
        }
        if (userCrudRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new EmailAlreadyExistException();
        }
        if (userCrudRepository.existsByUsername(request.getUsername())) {
            throw new UsernameOrEmailUsedToExistException();
        }
        if (userCrudRepository.existsByEmail(request.getEmail())) {
            throw new UsernameOrEmailUsedToExistException();
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

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
