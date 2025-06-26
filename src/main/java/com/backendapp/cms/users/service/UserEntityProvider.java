package com.backendapp.cms.users.service;

import com.backendapp.cms.users.entity.UserEntity;
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
        UserEntity user = userCrudRepository.findByUsername(identifier) // Mencari berdasarkan username
                .orElseGet(() -> userCrudRepository.findByEmail(identifier) // Jika tidak ditemukan, cari berdasarkan email
                        .orElseThrow(() -> new UsernameNotFoundException("Username atau email")));

        return user;
    }
}
