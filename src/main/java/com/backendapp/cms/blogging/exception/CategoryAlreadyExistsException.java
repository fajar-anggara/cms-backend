package com.backendapp.cms.blogging.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException() {
        super("Kategori dengan nama tersebuh sudah tersedia");
    }
}
