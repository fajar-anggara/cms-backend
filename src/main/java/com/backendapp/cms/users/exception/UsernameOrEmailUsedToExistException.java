package com.backendapp.cms.users.exception;

public class UsernameOrEmailUsedToExistException extends RuntimeException{
    public UsernameOrEmailUsedToExistException() {
        super("Username atau Email sudah pernah didaftarkan, hubungi admin untuk mengaktifkan nya kembali");
    }
}
