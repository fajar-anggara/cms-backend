package com.backendapp.cms.security.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() {
        super("Refresh token tidak ditemukan");
    }
}
