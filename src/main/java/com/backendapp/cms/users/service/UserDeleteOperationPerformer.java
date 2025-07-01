package com.backendapp.cms.users.service;

import com.backendapp.cms.common.exception.ResourceNotFoundException;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserDeleteOperationPerformer {

    private UserCrudRepository userCrudRepository;

    public UserDeleteOperationPerformer(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Transactional
    public void deleteUser(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        UserEntity deletingUser = userCrudRepository.findByUsernameAndDeletedAtIsNull(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        deletingUser.setDeletedAt(LocalDateTime.now());
        userCrudRepository.save(deletingUser);
    }
}
