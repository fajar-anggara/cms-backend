package com.backendapp.cms.security.exception;

public class ExpiredRefreshPasswordTokenException extends RuntimeException{
    public ExpiredRefreshPasswordTokenException() {
        super("Token yang anda masukan sudah kadaluarsa.");
    }
}
