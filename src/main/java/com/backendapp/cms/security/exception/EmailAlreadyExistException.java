package com.backendapp.cms.security.exception;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException() {
        super("Email sudah terdaftar");
    }
}
