package com.backendapp.cms.users.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super("Username tidak terdaftar");
    }
}
