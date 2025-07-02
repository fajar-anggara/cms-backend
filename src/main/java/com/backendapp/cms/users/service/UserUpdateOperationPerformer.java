package com.backendapp.cms.users.service;

import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.security.validation.annotation.UniqueUser;
import com.backendapp.cms.users.converter.UserUpdateConverter;
import com.backendapp.cms.users.dto.UserUpdateDto;
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
public class UserUpdateOperationPerformer {

    private final UserCrudRepository userCrudRepository;

    @Transactional
    public UserEntity updateUser(UserEntity userBeingUpdated, @Valid UserUpdateDto updateRequest) {

        log.info("Sanitating updating data");
        updateRequest.getUsername().ifPresent(userBeingUpdated::setUsername);
        updateRequest.getDisplayName().ifPresent(userBeingUpdated::setDisplayName);
        updateRequest.getEmail().ifPresent(userBeingUpdated::setEmail);
        updateRequest.getBio().ifPresent(userBeingUpdated::setBio);
        updateRequest.getProfilePicture().ifPresent(userBeingUpdated::setProfilePicture);
        updateRequest.getEnabled().ifPresent(userBeingUpdated::setEnabled);
        updateRequest.getIsEmailVerified().ifPresent(userBeingUpdated::setEmailVerified);

        return userCrudRepository.save(userBeingUpdated);
    }
}
