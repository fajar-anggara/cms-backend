package com.backendapp.cms.security.exception;

public class EmailUsedToExistsException extends RuntimeException{
    public EmailUsedToExistsException() {
        super("Email sudah pernah terdaftar");
    }
}
