package com.backendapp.cms.users.exception.handler;

import com.backendapp.cms.common.dto.ErrorResponse;
import com.backendapp.cms.users.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameAlreadyExistException(UsernameAlreadyExistException e, WebRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handle_EmailAlreadyExistException(EmailAlreadyExistException e, WebRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("email", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameNotFoundException(UsernameNotFoundException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_EmailNotFoundException(EmailNotFoundException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("email", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameOrEmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameOrEmailNotFoundException(UsernameOrEmailNotFoundException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("identifier", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        String message;
        HashMap<String, String> errors = null;

        if (ex.getCause() instanceof UsernameOrEmailNotFoundException) {
            message = ex.getCause().getMessage();
            errors = new HashMap<>();
            errors.put("identifier", message);
            return new ResponseEntity<>(new ErrorResponse(false, message, errors), HttpStatus.UNAUTHORIZED);
        } else {
            message = "Authentication failed: " + ex.getMessage();
            return new ResponseEntity<>(new ErrorResponse(false, message), HttpStatus.UNAUTHORIZED);
        }
    }
}