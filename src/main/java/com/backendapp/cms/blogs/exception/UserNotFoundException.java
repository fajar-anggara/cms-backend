package com.backendapp.cms.blogs.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User tidak ditemukan");
    }
}
