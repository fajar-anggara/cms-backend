package com.backendapp.cms.users.service;

import com.backendapp.cms.openapi.dto.UserUpdateRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
@Slf4j
public class UserUpdateOperationPerformer {

    private final UserCrudRepository userCrudRepository;
    private final UserUpdateConverter userUpdateConverter;

    @Transactional
    public UserEntity updateUser(
            Authentication authentication,
            @Valid
            @RequestBody
            @UniqueUser
            @UniqueEmail
            UserUpdateRequest request) {
        UserEntity userBeingUpdated = (UserEntity) authentication.getPrincipal();

        log.info("Sanitating updating data");
        UserUpdateDto updateRequestData = userUpdateConverter.toUserUpdateDto(request);
        updateRequestData.getUsername().ifPresent(userBeingUpdated::setUsername);
        updateRequestData.getDisplayName().ifPresent(userBeingUpdated::setDisplayName);
        updateRequestData.getEmail().ifPresent(userBeingUpdated::setEmail);
        updateRequestData.getBio().ifPresent(userBeingUpdated::setBio);
        updateRequestData.getProfilePicture().ifPresent(userBeingUpdated::setProfilePicture);
        updateRequestData.getEnabled().ifPresent(userBeingUpdated::setEnabled);
        updateRequestData.getIsEmailVerified().ifPresent(userBeingUpdated::setEmailVerified);

        return userCrudRepository.save(userBeingUpdated);
    }
}
