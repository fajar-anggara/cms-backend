package com.backendapp.cms.security.validation;


import com.backendapp.cms.security.exception.PasswordMismatchException;

public class PasswordMatchValidator {
    public static String check(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }

        return password;
    }
}
