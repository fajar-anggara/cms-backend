package com.backendapp.cms.common.exception.handler;

import com.backendapp.cms.common.dto.ErrorResponse;
import com.backendapp.cms.security.exception.*;
import com.backendapp.cms.security.exception.RefreshTokenExpiredException;
import com.backendapp.cms.users.exception.EmailNotFoundException;
import com.backendapp.cms.users.exception.*;
import com.backendapp.cms.common.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.ElementCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * User exceptions handler
     * |
     * handle_EmailAlreadyExistException
     * handle_EmailNotFoundException
     * handle_UsernameAlreadyExistException
     * handle_UsernameNotFoundException
     * handle_UsernameOrEmailNotFoundException
     * handle_UsernameOrEmailUsedToExistException
     * handle_usernameUsedToExistsException
     * handle_emailUsedToExistsException
     * handle_wrongRefreshPasswordTokenException
     * handle_ExpiresRefreshPasswordTokenException
     * |
     * handle_BadCredentialsException
     * handle_MethodArgumentNotValidException
     */


    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handle_EmailAlreadyExistException(EmailAlreadyExistException e) {
        log.warn("EmailAlreadyExistException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("email", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_EmailNotFoundException(EmailNotFoundException e) {
        log.warn("EmailNotFoundException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("email", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameAlreadyExistException(UsernameAlreadyExistException e) {
        log.warn("UsernameAlreadyExistException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("UsernameNotFoundException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameOrEmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameOrEmailNotFoundException(UsernameOrEmailNotFoundException e) {
        log.warn("UsernameOrEmailNotFoundException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("identifier", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameOrEmailUsedToExistException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameOrEmailUsedToExistException(UsernameOrEmailUsedToExistException e) {
        log.warn("UsernameOrEmailUsedToExistException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameUsedToExistsException.class)
    public ResponseEntity<ErrorResponse> handle_usernameUsedToExistsException(UsernameUsedToExistsException e) {
        log.warn("usernameUsedToExistsException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailUsedToExistsException.class)
    public ResponseEntity<ErrorResponse> handle_emailUsedToExistsException(EmailUsedToExistsException e) {
        log.warn("emailUsedToExistsException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WrongPasswordRefreshToken.class)
    public ResponseEntity<ErrorResponse> handle_WrongPasswordRefreshToken(WrongPasswordRefreshToken e) {
        log.warn("WrongPasswordRefreshToken");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("refreshPasswordToken", e.getMessage());

        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredRefreshPasswordTokenException.class)
    public ResponseEntity<ErrorResponse> handle_ExpiredRefreshPasswordTokenException(ExpiredRefreshPasswordTokenException e) {
        log.warn("ExpiredRefreshPasswordTokenException");
        ErrorResponse error = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handle_BadCredentialsException(BadCredentialsException e) {
        log.warn("BadCredentialsException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", "Username atau password salah");
        errors.put("password", "Username atau password salah");

        ErrorResponse errorResponse = new ErrorResponse(false, "Username atau password salah", errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle_MethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("MethodArgumentNotValidException");
        HashMap<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(false, "Validation failed. Please check your input.", fieldErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * Security exception handler
     * |
     * handle_AuthenticationExceptions
     * handle_JwtExceptions
     * handle_expiredJwtException
     * handle_AccessDeniedException
     * handle_ResourceNotFoundException
     * handle_PasswordMismatchException
     * handle_accessAndRefreshTokenMismatchException
     * handle_refreshTokenNotFoundException
     * handle_refreshTokenExpiresException
     * handle_userIsDisableException
     * handle_invalidAccessTokenException
     * handle_invalidRefreshTokenException
     * |
     * handle_InternalAuthenticationServiceException
     * handle_AllUncaughtExceptions
     */


    @ExceptionHandler({
            DisabledException.class,
            LockedException.class,
    })
    public ResponseEntity<ErrorResponse> handle_AuthenticationExceptions(Exception ex) {
        log.warn("DisabledException || LockedException");
        String message = "Authentication failed. Invalid credentials.";
        if (ex instanceof DisabledException) {
            message = "Your account is disabled.";
        } else if (ex instanceof LockedException) {
            message = "Your account is locked.";
        }

        ErrorResponse errorResponse = new ErrorResponse(false, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            MalformedJwtException.class,
            SignatureException.class
    })
    public ResponseEntity<ErrorResponse> handle_JwtExceptions(Exception ex) {
        log.warn("MalformedJwtException || SignatureException");
        String message = "Authentication failed: Invalid or expired token.";
        if (ex instanceof SignatureException) {
            message = "Authentication failed: Invalid token signature.";
        } else if (ex instanceof MalformedJwtException) {
            message = "Authentication failed: Malformed token.";
        }

        ErrorResponse errorResponse = new ErrorResponse(false, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handle_ExpiredJwtException(ExpiredJwtException ex) {
        log.warn("ExpiredJwtException");
        ErrorResponse errorResponse = new ErrorResponse(false, "Token kadaluarsa. lakukan refresh token atau login kembali");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handle_AccessDeniedException(AccessDeniedException ex) {
        log.warn("AccessDeniedException");
        ErrorResponse errorResponse = new ErrorResponse(false, "Access Denied. You do not have permission to access this resource.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_ResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("ResourceNotFoundException");
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handle_PasswordMismatchException(PasswordMismatchException e) {
        log.warn("PasswordMismatchException");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("password_confirm", "Password Tidak Sesuai.");

        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessAndRefreshTokenMismatchException.class)
    public ResponseEntity<ErrorResponse> handle_AccessAndRefreshTokenMismatchException(AccessAndRefreshTokenMismatchException e) {
        log.warn("AccessAndRefreshTokenMismatchException");
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_refreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        log.warn("RefreshTokenNotFoundException");
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handle_AccessAndRefreshTokenMismatchException(RefreshTokenExpiredException e) {
        log.warn("refreshTokenExpiredException");
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserIsDisabledException.class)
    public ResponseEntity<ErrorResponse> handle_AccessAndRefreshTokenMismatchException(UserIsDisabledException e) {
        log.warn("UserIsDisabledException");
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    public ResponseEntity<ErrorResponse> handle_InvalidAccessTokenException(InvalidAccessTokenException e) {
        log.warn("InvalidAccessTokenException");
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handle_InvalidRefreshTokenException(InvalidRefreshTokenException e) {
        log.warn("InvalidRefreshTokenException");
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }





















    // =================================================================================================================

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handle_InternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        log.warn("InternalAuthenticationServiceException");
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle_AllUncaughtExceptions(Exception ex) {
        log.warn("AllUncaughtExceptions");
        System.err.println("An unexpected error occurred: " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(false, "An unexpected internal server error occurred. Please try again later.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}