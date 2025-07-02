package com.backendapp.cms.security.validation.annotationValidator;

import com.backendapp.cms.security.validation.annotation.UniqueUser;
import com.backendapp.cms.security.exception.UsernameUsedToExistsException;
import com.backendapp.cms.users.exception.UsernameAlreadyExistException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {

    private final UserCrudRepository userCrudRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username.isBlank()) {
            return true;
        }

        log.info("validating unique user");
        if (userCrudRepository.existsByUsernameAndDeletedAtIsNull(username)) {
            log.warn("User {} already exists", username);
            throw new UsernameAlreadyExistException();
        }
        if (userCrudRepository.existsByUsername(username)) {
            log.warn("User {} used to exists", username);
            throw new UsernameUsedToExistsException();
        }

        return true;
    }

    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
