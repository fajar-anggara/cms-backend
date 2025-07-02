package com.backendapp.cms.security.exception;

public class UsernameUsedToExistsException extends RuntimeException{
    public UsernameUsedToExistsException() {
        super("Username sudah pernah terdaftar");
    }
}
