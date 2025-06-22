package com.backendapp.cms.security.service;

import com.backendapp.cms.common.constant.UserConstants;
import com.backendapp.cms.openapi.dto.UserRegister200Response;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.security.converter.RegistrationConverter;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.users.dto.UserEntityDto;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationOperationPerformer {
    private final PasswordEncoder passwordEncoder;
    private final UserCrudRepository userCrudRepository;

    private UserRegistrationOperationPerformer(
            PasswordEncoder passwordEncoder,
            UserCrudRepository userCrudRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userCrudRepository = userCrudRepository
    }

    public  UserEntityDto registerUser(@Valid UserRegisterRequest request) {
        // validasi conflict
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = RegistrationConverter.toEntity(request);
        user.setPassword(encryptedPassword);
        user.setProfilePicture(UserConstants.DEFAULT_PROFILE_PICTURE);
        user.setBio(UserConstants.DEFAULT_BIO);
        user.setRole(UserConstants.DEFAULT_ROLE);
        user.setEnabled(UserConstants.DEFAULT_ENABLE);
        user.setEmailVerified(UserConstants.DEFAULT_EMAIL_VERIFIED);

        UserEntity userEntity = userCrudRepository.save(user);
        return RegistrationConverter.toDto(userEntity);
    }

    public ResponseEntity<UserRegister200Response> getResponse(UserEntityDto user) {

    }

    {

        String encryptedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
        UserGrantedAuthority defaultUserGrantedAuthority = UserGrantedAuthority.builder().authority(Authority.USER).build();

        UserEntity newUserEntity = registrationDtoConverter.toEntity(userRegistrationRequest);
        newUserEntity.setPassword(encryptedPassword);
        newUserEntity.addAuthority(defaultUserGrantedAuthority);
        newUserEntity.setAccountNonExpired(DEFAULT_ACCOUNT_NON_EXPIRED);
        newUserEntity.setAccountNonLocked(DEFAULT_ACCOUNT_NON_LOCKED);
        newUserEntity.setCredentialsNonExpired(DEFAULT_CREDENTIALS_NON_EXPIRED);
        newUserEntity.setEnabled(DEFAULT_ENABLED);

        UserEntity userEntity = userCrudRepository.save(newUserEntity);

        final String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
        final String jwtToken = jwtTokenProvider.generateToken(userEntity);

        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setToken(jwtToken);
        response.setRefreshToken(jwtRefreshToken);
        return response;
    }
}
