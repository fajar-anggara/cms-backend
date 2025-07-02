package com.backendapp.cms.superuser.service;

import com.backendapp.cms.users.dto.UserUpdateDto;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.service.UserUpdateOperationPerformer;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class SuperuserUpdateOperationPerformer {

    private final UserUpdateOperationPerformer userUpdateOperationPerformer;

    public UserEntity updateUser(UserEntity userBeingUpdated, @Valid UserUpdateDto updateRequest) {

        /*log.info("sanitating update request by superuser");
        updateRequest.getUsername().ifPresent(userBeingUpdated::setUsername);
        updateRequest.getDisplayName().ifPresent(userBeingUpdated::setDisplayName);
        updateRequest.getEmail().ifPresent(userBeingUpdated::setEmail);
        updateRequest.getBio().ifPresent(userBeingUpdated::setBio);
        updateRequest.getProfilePicture().ifPresent(userBeingUpdated::setProfilePicture);
        updateRequest.getEnabled().ifPresent(userBeingUpdated::setEnabled);
        updateRequest.getIsEmailVerified().ifPresent(userBeingUpdated::setEmailVerified);*/

        return userUpdateOperationPerformer.updateUser(userBeingUpdated, updateRequest);

    }
}
