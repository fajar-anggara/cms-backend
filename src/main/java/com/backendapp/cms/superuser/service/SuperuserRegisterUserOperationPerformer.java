package com.backendapp.cms.superuser.service;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.security.validation.PasswordMatchValidator;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.security.repository.UserGrantedAuthorityRepository;
import com.backendapp.cms.users.dto.UserRegisterDto;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class SuperuserRegisterUserOperationPerformer {

    private final UserCrudRepository userCrudRepository;
    private final UserGrantedAuthorityRepository userGrantedAuthorityRepository;
    private final PasswordMatchValidator passwordMatchValidator;

    @Transactional
    public UserEntity registerUser(@Valid UserRegisterDto userRequest) {

        log.info("encrypt password, and saved it. -- this is superuser");
        String encryptedPassword = passwordMatchValidator.check(userRequest.getPassword(), userRequest.getConfirmPassword());

        UserEntity user = new UserEntity();
        userRequest.getUsername().ifPresent(user::setUsername);
        userRequest.getDisplayName().ifPresent(user::setDisplayName);
        userRequest.getEmail().ifPresent(user::setEmail);
        user.setPassword(encryptedPassword);
        user.setEnabled(userRequest.getEnabled());
        user.setEmailVerified(userRequest.getEmailVerified());
        user.setAuthority(setGrantedAuthority(userRequest.getAuthority()));

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
