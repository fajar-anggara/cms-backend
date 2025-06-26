package com.backendapp.cms.users.service;// CustomUserDetailsService.java
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
// ...

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserCrudRepository userCrudRepository;

    public CustomUserDetailsService(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        logger.info("Attempting to load user with identifier: {}", identifier);

        // Coba cari berdasarkan username
        Optional<UserEntity> userByUsername = userCrudRepository.findByUsername(identifier);
        if (userByUsername.isPresent()) {
            logger.info("User found by username: {}", identifier);
            return userByUsername.get();
        }

        logger.info("User not found by username. Attempting to find by email: {}", identifier);
        // Jika tidak ditemukan, coba cari berdasarkan email
        Optional<UserEntity> userByEmail = userCrudRepository.findByEmail(identifier);
        if (userByEmail.isPresent()) {
            logger.info("User found by email: {}", identifier);
            return userByEmail.get();
        }

        // Jika keduanya tidak ada
        logger.warn("User not found with username or email: {}", identifier);
        throw new UsernameNotFoundException("User not found with username or email: " + identifier);
    }
}