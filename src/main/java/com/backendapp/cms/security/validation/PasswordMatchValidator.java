package com.backendapp.cms.security.validation;


import com.backendapp.cms.security.exception.PasswordMismatchException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PasswordMatchValidator {

    private final PasswordEncoder passwordEncoder;

    public String check(Optional<String> password, Optional<String> confirmPassword) {
        if (!password.get().equals(confirmPassword.get())) {
            throw new PasswordMismatchException();
        }

        return passwordEncoder.encode(password.get());
    }
}
