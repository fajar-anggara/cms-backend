package com.backendapp.cms.security.service;

import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserEntityProvider {

    private final UserCrudRepository userCrudRepository;

    public UserEntity getUser(String identifier) {
        return userCrudRepository.findByUsername(identifier)
                .orElseGet(() -> userCrudRepository.findByEmail(identifier)
                        .orElseThrow(UsernameOrEmailNotFoundException::new));
    }
}
