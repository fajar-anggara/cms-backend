package com.backendapp.cms.security.validation.annotationValidator;

import com.backendapp.cms.security.validation.annotation.UniqueUser;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, Optional<String>> {

    private final UserCrudRepository userCrudRepository;
    private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    public UniqueUserValidator(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public boolean isValid(Optional<String> username, ConstraintValidatorContext context) {
        log.info("username validation excecuted");
        if(username.isEmpty()) {
            return true;
        }
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            if (auth instanceof UserDetails userDetails) {
                if (username.get().equals(userDetails.getUsername())) {
                    return true;
                }
            }
        }
        if (userCrudRepository.existsByUsernameAndDeletedAtIsNull(username.get())) {
            log.warn("User {} already exists", username.get());
            return false;
        }
        if (userCrudRepository.existsByUsername(username.get())) {
            log.warn("User {} used to exists", username.get());
            return false;
        }

        return true;
    }

    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        /*issuer = constraintAnnotation.issuer();*/
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
