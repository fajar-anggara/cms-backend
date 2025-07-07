package com.backendapp.cms.common.config;

import com.backendapp.cms.common.constant.AppConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.security.service.UserRegistrationOperationPerformer;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class SuperUserInitializer implements CommandLineRunner {

    private final UserCrudRepository userCrudRepository;
    private final AppConstants appConstants;
    private final PasswordEncoder passwordEncoder;
    private final UserRegistrationOperationPerformer userRegistrationOperationPerformer;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Try to create default superuser");
        // Checking default superuser
        if(!userCrudRepository.existsByUsernameAndDeletedAtIsNull("superuser")) {
            String password = passwordEncoder.encode(appConstants.SUPERUSER_PASSWORD);
            UserEntity user = UserEntity.builder()
                    .username(appConstants.SUPERUSER_USERNAME)
                    .displayName(appConstants.SUPERUSER_DISPLAY_NAME)
                    .email(appConstants.SUPERUSER_EMAIL)
                    .authority(userRegistrationOperationPerformer.setGrantedAuthority(Authority.ADMIN))
                    .enabled(true)
                    .password(password)
                    .build();

            UserEntity superuser = userCrudRepository.save(user);
            log.info("Success creating superuser with name {}", superuser.getUsername());
        } else {
            System.out.println("Superuser already exists");
        }
    }
}
