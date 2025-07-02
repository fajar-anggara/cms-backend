package com.backendapp.cms.security.validation.annotationValidator;

import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserCrudRepository userCrudRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        log.info("validating email triggered");
        if (userCrudRepository.existsByEmailAndDeletedAtIsNull(email)) {
            log.warn("Email {} already exists", email);
            context.buildConstraintViolationWithTemplate("Email '" + email + "' sudah terdaftar.").addConstraintViolation();
            return false; // Tidak valid
        }
        if (userCrudRepository.existsByEmail(email)) {
            log.warn("Email {} used to exists", email);
            context.buildConstraintViolationWithTemplate("Email '" + email + "' sudah pernah terdaftar.").addConstraintViolation();
            return false; // Tidak valid
        }

        return true;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
