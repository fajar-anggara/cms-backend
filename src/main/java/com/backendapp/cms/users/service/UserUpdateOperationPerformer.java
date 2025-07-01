package com.backendapp.cms.users.service;

import com.backendapp.cms.openapi.dto.UserUpdateRequest;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.security.exception.EmailAlreadyExistException;
import com.backendapp.cms.users.exception.UsernameAlreadyExistException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class UserUpdateOperationPerformer {

    private final UserCrudRepository userCrudRepository;

    @Transactional
    public UserEntity updateUser(Authentication authentication, @Valid @RequestBody UserUpdateRequest request) {
        UserEntity userBeingUpdated = (UserEntity) authentication.getPrincipal();
        if (request.getEnabled() != null && request.getEnabled()) {
            userBeingUpdated.setEnabled(request.getEnabled());
        }
        if(request.getUsername() != null && !request.getUsername().isEmpty()) {
            verifiedNotExistUsername(request.getUsername(), userBeingUpdated);
            userBeingUpdated.setUsername(request.getUsername());
        }
        if (request.getIsEmailVerified() != null && request.getIsEmailVerified()) {
            userBeingUpdated.setEmailVerified(true);
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            verifiedNotExistEmail(request.getEmail(), userBeingUpdated);
            userBeingUpdated.setEmail(request.getEmail());
        }
        if (request.getBio() != null && !request.getBio().isEmpty()) {
            userBeingUpdated.setBio(request.getBio());
        }
        if (request.getDisplayName() != null && !request.getDisplayName().isEmpty()) {
            userBeingUpdated.setDisplayName(request.getDisplayName());
        }
        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            userBeingUpdated.setProfilePicture(request.getProfilePicture());
        }
        return userCrudRepository.save(userBeingUpdated);
    }

    private void verifiedNotExistUsername(String username, UserEntity userBeingUpdated) {
        if (username.equals(userBeingUpdated.getUsername())) {
            return;
        }
        if (userCrudRepository.existsByUsernameAndDeletedAtIsNull(username)) {
            throw new UsernameAlreadyExistException();
        }
    }

    private void verifiedNotExistEmail(String email, UserEntity userBeingUpdated) {
        if (email.equals(userBeingUpdated.getEmail())) {
            return;

        }        if (userCrudRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new EmailAlreadyExistException();
        }
    }
}
