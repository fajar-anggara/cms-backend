package com.backendapp.cms.security.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Password Tidak Sesuai.");
    }
}
