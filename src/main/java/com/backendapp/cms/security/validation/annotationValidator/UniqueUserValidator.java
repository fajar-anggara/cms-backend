package com.backendapp.cms.security.validation.annotationValidator;

import com.backendapp.cms.security.validation.annotation.UniqueUser;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {

    private final UserCrudRepository userCrudRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        log.info("username validation excecuted");
        if (userCrudRepository.existsByUsernameAndDeletedAtIsNull(username)) {
            log.warn("User {} already exists", username);
            context.buildConstraintViolationWithTemplate("Email '" + username + "' sudah terdaftar.").addConstraintViolation();
            return false; // Tidak valid
        }
        if (userCrudRepository.existsByUsername(username)) {
            log.warn("User {} used to exists", username);
            context.buildConstraintViolationWithTemplate("Email '" + username + "' sudah pernah terdaftar.").addConstraintViolation();
            return false; // Tidak valid
        }

        return true;
    }

    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
