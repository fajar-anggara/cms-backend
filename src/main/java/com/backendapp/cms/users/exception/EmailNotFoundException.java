package com.backendapp.cms.users.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("Email tidak terdaftar");
    }
}
