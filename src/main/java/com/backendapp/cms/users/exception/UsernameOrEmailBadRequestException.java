package com.backendapp.cms.users.exception;

public class UsernameOrEmailBadRequestException extends RuntimeException {
    public UsernameOrEmailBadRequestException(String type) {
        super(type);
    }
}
