package com.backendapp.cms.users.exception;

public class UsernameOrEmailNotFoundException extends RuntimeException {
    public UsernameOrEmailNotFoundException() {
        super("Username atau Email tidak terdaftar");
    }
}
