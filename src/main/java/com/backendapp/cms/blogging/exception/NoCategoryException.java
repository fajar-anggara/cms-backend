package com.backendapp.cms.blogging.exception;

public class NoCategoryException extends RuntimeException {
    public NoCategoryException() {
        super("Category tidak tersedia");
    }
}
