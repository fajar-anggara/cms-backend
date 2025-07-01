package com.backendapp.cms.security.service;

import com.backendapp.cms.common.constant.UserConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.security.repository.UserGrantedAuthorityRepository;
import com.backendapp.cms.users.converter.UserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.security.exception.EmailAlreadyExistException;
import com.backendapp.cms.users.exception.UsernameAlreadyExistException;
import com.backendapp.cms.users.exception.UsernameOrEmailUsedToExistException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserRegistrationOperationPerformer {
    private final PasswordEncoder passwordEncoder;
    private final UserCrudRepository userCrudRepository;
    private final UserGrantedAuthorityRepository userGrantedAuthorityRepository;
    private final UserConverter userConverter;

    @Transactional
    public UserEntity registerUser(@Valid UserRegisterRequest request) {
        log.info("Validating register request, then setting authority, encrypt password, and saved it.");
        if (userCrudRepository.existsByUsernameAndDeletedAtIsNull(request.getUsername())) {
            throw new UsernameAlreadyExistException();
        }
        if (userCrudRepository.existsByUsername(request.getUsername())) {
            throw new UsernameOrEmailUsedToExistException();
        }
        if (userCrudRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new EmailAlreadyExistException();
        }
        if (userCrudRepository.existsByEmail(request.getEmail())) {
            throw new UsernameOrEmailUsedToExistException();
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }
        UserGrantedAuthority defaultAuthority = setGrantedAuthority(UserConstants.DEFAULT_ROLE);
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = userConverter.toEntity(request);
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
