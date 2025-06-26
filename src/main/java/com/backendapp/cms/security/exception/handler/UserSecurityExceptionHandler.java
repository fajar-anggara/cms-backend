package com.backendapp.cms.security.exception.handler;

import com.backendapp.cms.common.dto.ErrorResponse;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
public class UserSecurityExceptionHandler {

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(PasswordMismatchException e, WebRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("password_confirm", "Password Tidak Sesuai.");

        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", "Username atau password salah");
        errors.put("password", "Username atau password salah");
        ErrorResponse errorResponse = new ErrorResponse(false, "Username atau password salah", errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}