package com.backendapp.cms.users.service;

import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserEntityProvider {

    private final UserCrudRepository userCrudRepository;

    public UserEntityProvider(
            UserCrudRepository userCrudRepository
    ) {
        this.userCrudRepository = userCrudRepository;
    }

    public UserEntity getUser(String identifier) {

        return userCrudRepository.findByUsername(identifier)
                .orElseGet(() -> userCrudRepository.findByEmail(identifier)
                        .orElseThrow(UsernameOrEmailNotFoundException::new));
    }
}
