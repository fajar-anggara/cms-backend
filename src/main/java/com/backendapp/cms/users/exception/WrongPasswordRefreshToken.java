package com.backendapp.cms.users.exception;

public class WrongPasswordRefreshToken extends RuntimeException{
    public WrongPasswordRefreshToken() {
        super("Reset password token yang anda masukan salah.");
    }
}
