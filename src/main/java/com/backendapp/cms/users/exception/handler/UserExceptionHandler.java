package com.backendapp.cms.users.exception.handler;

import com.backendapp.cms.common.dto.ErrorResponse;
import com.backendapp.cms.users.exception.AlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
public class UserExceptionHandler {
    HashMap<String, String > errors = new HashMap<>();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(MethodArgumentNotValidException e, WebRequest request) {
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e, WebRequest request) {
        errors.put(e.getMessage(), e.getMessage() + " sudah terdaftar");
        ErrorResponse error = new ErrorResponse(true, e.getMessage() + " sudah terdaftar", errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
