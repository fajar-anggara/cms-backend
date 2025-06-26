package com.backendapp.cms.users.exception;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException() {
        super("Username sudah terdaftar");
    }
}
