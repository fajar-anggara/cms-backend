package com.backendapp.cms.users.exception;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException() {
        super("Email sudah terdaftar");
    }
}
