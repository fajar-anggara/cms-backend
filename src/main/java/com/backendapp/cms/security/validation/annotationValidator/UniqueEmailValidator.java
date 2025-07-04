package com.backendapp.cms.security.validation.annotationValidator;

import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Optional<String>> {

    private final UserCrudRepository userCrudRepository;

    public UniqueEmailValidator(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public boolean isValid(Optional<String> email, ConstraintValidatorContext context) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (email.isEmpty() || auth.getPrincipal().toString().equals("anonymousUser")) {
            return true;
        }
        if (auth.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            UserEntity user = userCrudRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(UsernameNotFoundException::new);
            if (email.get().equals(user.getEmail())) {
                return true;
            }
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
        /*this.issuer = constraintAnnotation.issuer();*/
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
