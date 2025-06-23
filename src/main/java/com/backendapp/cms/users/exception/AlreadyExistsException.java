package com.backendapp.cms.users.exception;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String field) {
        super(field + " sudah terdaftar");
    }
}
