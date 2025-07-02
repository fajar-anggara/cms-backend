package com.backendapp.cms.security.validation.annotationValidator;

import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.security.exception.EmailAlreadyExistException;
import com.backendapp.cms.security.exception.EmailUsedToExistsException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserCrudRepository userCrudRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email.isBlank()) {
            return true;
        }

        log.info("validating unique email");
        if (userCrudRepository.existsByEmailAndDeletedAtIsNull(email)) {
            log.warn("Email {} already exists", email);
            throw new EmailAlreadyExistException();
        }
        if (userCrudRepository.existsByEmail(email)) {
            log.warn("Email {} used to exists", email);
            throw new EmailUsedToExistsException();
        }

        return true;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
