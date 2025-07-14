package com.backendapp.cms.blogging.exception;

public class NoPostsException extends RuntimeException {
    public NoPostsException() {
        super("Tidak ada postingan");
    }
}
