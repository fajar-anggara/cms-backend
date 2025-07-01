package com.backendapp.cms.users.service;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
// ...

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCrudRepository userCrudRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameOrEmailNotFoundException {
        log.info("Custom loadUserByUsername executed by spring security authenticating");
        Optional<UserEntity> userByUsername = userCrudRepository.findByUsernameAndDeletedAtIsNull(identifier);
        if (userByUsername.isPresent()) {
            return userByUsername.get();
        }

        Optional<UserEntity> userByEmail = userCrudRepository.findByEmailAndDeletedAtIsNull(identifier);
        if (userByEmail.isPresent()) {
            return userByEmail.get();
        }

        throw new UsernameOrEmailNotFoundException();
    }
}