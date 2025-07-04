package com.backendapp.cms.superuser.service;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.UpdateSingleUserRequest;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SuperuserUpdateOperationPerformer {

    private final SuperuserRegisterUserOperationPerformer superuserRegisterUserOperationPerformer;
    private final UserCrudRepository userCrudRepository;

    public UserEntity updateUser(UserEntity userBeingUpdated, UpdateSingleUserRequest updateRequest) {

        if(updateRequest.getEnabled() != null) {
            userBeingUpdated.setEnabled(updateRequest.getEnabled());
        }
        if (updateRequest.getEmailVerified() != null) {
            userBeingUpdated.setEmailVerified(updateRequest.getEmailVerified());
        }
        if(updateRequest.getAuthority() != null) {
            userBeingUpdated.setAuthority(superuserRegisterUserOperationPerformer.setGrantedAuthority(Authority.valueOf(updateRequest.getAuthority().toString())));
        }
        if(updateRequest.getDeletedAt() == null) {
            userBeingUpdated.setDeletedAt(null);
        } else if (!updateRequest.getDeletedAt().equals(userBeingUpdated.getDeletedAt())) {
            userBeingUpdated.setDeletedAt(updateRequest.getDeletedAt().toLocalDateTime());
        }

        return userCrudRepository.save(userBeingUpdated);

    }
}
