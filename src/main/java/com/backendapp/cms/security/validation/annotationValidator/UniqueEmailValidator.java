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
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Optional<String>> {

    private final UserCrudRepository userCrudRepository;

    @Override
    public boolean isValid(Optional<String> email, ConstraintValidatorContext context) {
        log.info("validating email triggered");
        if (email.isEmpty()) {
            return true;
        }
        if (userCrudRepository.existsByEmailAndDeletedAtIsNull(email.get())) {
            log.warn("Email {} already exists", email.get());
            return false;
        }
        if (userCrudRepository.existsByEmail(email.get())) {
            log.warn("Email {} used to exists", email);
            return false;
        }
        return true;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
