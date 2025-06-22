package com.backendapp.cms.users.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super("Bad request, ");
    }
}
