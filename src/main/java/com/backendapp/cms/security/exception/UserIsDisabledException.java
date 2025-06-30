package com.backendapp.cms.security.exception;

public class UserIsDisabledException extends RuntimeException {
    public UserIsDisabledException() {
        super("User sedang di disable");
    }
}
