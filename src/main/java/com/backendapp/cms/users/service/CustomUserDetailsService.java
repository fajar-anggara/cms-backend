package com.backendapp.cms.users.service;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
// ...

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCrudRepository userCrudRepository;

    public CustomUserDetailsService(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameOrEmailNotFoundException {

        // Berdasarkan username
        Optional<UserEntity> userByUsername = userCrudRepository.findByUsername(identifier);
        if (userByUsername.isPresent()) {
            return userByUsername.get();
        }

        // Jika tidak ditemukan, coba cari berdasarkan email
        Optional<UserEntity> userByEmail = userCrudRepository.findByEmail(identifier);
        if (userByEmail.isPresent()) {
            return userByEmail.get();
        }

        throw new UsernameOrEmailNotFoundException();
    }
}