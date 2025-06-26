package com.backendapp.cms.common.exception.handler;

import com.backendapp.cms.common.dto.ErrorResponse;
import com.backendapp.cms.users.exception.EmailAlreadyExistException;
import com.backendapp.cms.users.exception.UsernameAlreadyExistException;
import com.backendapp.cms.common.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", "Username atau password salah");
        errors.put("password", "Username atau password salah");

        ErrorResponse errorResponse = new ErrorResponse(false, "Username atau password salah", errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HashMap<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(false, "Validation failed. Please check your input.", fieldErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            DisabledException.class,
            LockedException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(Exception ex) {
        String message = "Authentication failed. Invalid credentials.";
        if (ex instanceof DisabledException) {
            message = "Your account is disabled.";
        } else if (ex instanceof LockedException) {
            message = "Your account is locked.";
        } else if (ex instanceof UsernameNotFoundException) {
            message = "User not found.";
        }

        ErrorResponse errorResponse = new ErrorResponse(false, message); // Menggunakan konstruktor tanpa errors
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            MalformedJwtException.class,
            ExpiredJwtException.class,
            SignatureException.class
    })
    public ResponseEntity<ErrorResponse> handleJwtExceptions(Exception ex) {
        String message = "Authentication failed: Invalid or expired token.";
        if (ex instanceof ExpiredJwtException) {
            message = "Authentication failed: Token has expired.";
        } else if (ex instanceof SignatureException) {
            message = "Authentication failed: Invalid token signature.";
        } else if (ex instanceof MalformedJwtException) {
            message = "Authentication failed: Malformed token.";
        }

        ErrorResponse errorResponse = new ErrorResponse(false, message); // Menggunakan konstruktor tanpa errors
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(false, "Access Denied. You do not have permission to access this resource.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            UsernameAlreadyExistException.class,
            EmailAlreadyExistException.class,
            // Tambahkan custom exception lain untuk konflik di sini
    })
    public ResponseEntity<ErrorResponse> handleConflictExceptions(Exception ex) {
        String message = "A conflict occurred with the existing resource.";
        HashMap<String, String> fieldErrors = null; // Inisialisasi null secara default
        if (ex instanceof UsernameAlreadyExistException) {
            message = ex.getMessage();
            fieldErrors = new HashMap<>(); // Buat HashMap hanya jika diperlukan
            fieldErrors.put("username", ex.getMessage());
        } else if (ex instanceof EmailAlreadyExistException) {
            message = ex.getMessage();
            fieldErrors = new HashMap<>(); // Buat HashMap hanya jika diperlukan
            fieldErrors.put("email", ex.getMessage());
        }

        // Menggunakan konstruktor yang sesuai
        ErrorResponse errorResponse = (fieldErrors != null && !fieldErrors.isEmpty()) ?
                new ErrorResponse(false, message, fieldErrors) :
                new ErrorResponse(false, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Tambahkan handler untuk 404 (ResourceNotFoundException) jika Anda memiliki custom exception ini
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtExceptions(Exception ex, WebRequest request) {
        System.err.println("An unexpected error occurred: " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(false, "An unexpected internal server error occurred. Please try again later.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}