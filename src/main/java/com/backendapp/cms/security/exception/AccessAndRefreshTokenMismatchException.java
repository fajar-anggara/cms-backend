package com.backendapp.cms.security.exception;

public class AccessAndRefreshTokenMismatchException extends RuntimeException{
    public AccessAndRefreshTokenMismatchException() {
        super("Access token dan refresh token tidak sama, silahkan login kembali");
    }
}
