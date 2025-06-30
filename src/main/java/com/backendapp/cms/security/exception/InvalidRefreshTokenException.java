package com.backendapp.cms.security.exception;

public class InvalidRefreshTokenException extends RuntimeException{
    public InvalidRefreshTokenException() {
        super("Refresh token tidak valid, harap login kembali.");
    }
}
