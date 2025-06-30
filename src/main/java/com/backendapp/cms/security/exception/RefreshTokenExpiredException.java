package com.backendapp.cms.security.exception;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() {
        super("Refresh token sudah kadaluarsa, harap login kembali");
    }
}
