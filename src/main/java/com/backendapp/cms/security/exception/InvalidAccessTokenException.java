package com.backendapp.cms.security.exception;

public class InvalidAccessTokenException extends RuntimeException{
    public InvalidAccessTokenException() {
        super("Access token tidak valid, harap melakukan request refresh token");
    }
}
