package com.backendapp.cms.security.service;

import com.backendapp.cms.common.constant.UserConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.security.repository.UserGrantedAuthorityRepository;
import com.backendapp.cms.users.converter.UserConverter;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.EmailAlreadyExistException;
import com.backendapp.cms.users.exception.UsernameAlreadyExistException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationOperationPerformer {
    private final PasswordEncoder passwordEncoder;
    private final UserCrudRepository userCrudRepository;
    private final UserGrantedAuthorityRepository userGrantedAuthorityRepository;
    private final UserConverter userConverter;

    public UserRegistrationOperationPerformer(
            PasswordEncoder passwordEncoder,
            UserCrudRepository userCrudRepository,
            UserGrantedAuthorityRepository userGrantedAuthorityRepository,
            UserConverter userConverter
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userCrudRepository = userCrudRepository;
        this.userGrantedAuthorityRepository = userGrantedAuthorityRepository;
        this.userConverter = userConverter;
    }

    @Transactional
    public UserEntity registerUser(@Valid UserRegisterRequest request) {
        if (userCrudRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException();
        }
        if (userCrudRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException();
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        UserGrantedAuthority defaultAuthority = userGrantedAuthorityRepository.findByAuthority(UserConstants.DEFAULT_ROLE)
                .orElseGet(() -> {
                    UserGrantedAuthority newAuthority = UserGrantedAuthority.builder()
                            .authority(Authority.BLOGGER)
                            .build();

                    return userGrantedAuthorityRepository.save(newAuthority);
                });
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
}
