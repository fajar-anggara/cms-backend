package com.backendapp.cms.common.exception.handler;

import com.backendapp.cms.blogging.exception.CategoryAlreadyExistsException;
import com.backendapp.cms.blogging.exception.NoCategoryException;
import com.backendapp.cms.blogging.exception.NoPostsException;
import com.backendapp.cms.common.dto.ErrorResponse;
import com.backendapp.cms.security.exception.*;
import com.backendapp.cms.security.exception.RefreshTokenExpiredException;
import com.backendapp.cms.users.exception.EmailNotFoundException;
import com.backendapp.cms.users.exception.*;
import com.backendapp.cms.common.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * User exceptions handler
     * |
     * handle_EmailNotFoundException
     * handle_UsernameNotFoundException
     * handle_UsernameOrEmailNotFoundException
     * handle_wrongRefreshPasswordTokenException
     * handle_expiresRefreshPasswordTokenException
     * handle_constraintViolationException
     * |
     * handle_BadCredentialsException
     * handle_MethodArgumentNotValidException
     */



    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_EmailNotFoundException(EmailNotFoundException e) {
        log.warn("EmailNotFoundException, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("email", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("UsernameNotFoundException, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameOrEmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_UsernameOrEmailNotFoundException(UsernameOrEmailNotFoundException e) {
        log.warn("UsernameOrEmailNotFoundException, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("identifier", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongPasswordRefreshToken.class)
    public ResponseEntity<ErrorResponse> handle_WrongPasswordRefreshToken(WrongPasswordRefreshToken e) {
        log.warn("WrongPasswordRefreshToken, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("refreshPasswordToken", e.getMessage());

        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredRefreshPasswordTokenException.class)
    public ResponseEntity<ErrorResponse> handle_ExpiredRefreshPasswordTokenException(ExpiredRefreshPasswordTokenException e) {
        log.warn("ExpiredRefreshPasswordTokenException, message {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle_ConstraintViolationException(ConstraintViolationException ex) {
        log.warn("ConstraintViolationException, message {}", ex.getMessage());
        HashMap<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> {
                            String path = violation.getPropertyPath().toString();
                            int lastDotIndex = path.lastIndexOf('.');
                            return (lastDotIndex > -1 && lastDotIndex < path.length() - 1)
                                    ? path.substring(lastDotIndex + 1)
                                    : path;
                        },
                        ConstraintViolation::getMessage,
                        (existingMessage, newMessage) -> existingMessage + "; " + newMessage,
                        HashMap::new
                ));

        ErrorResponse error = new ErrorResponse(false, "Validation failed", errors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * Blogging exception handler
     * |
     * handle_categoryAlreadyExistsException
     * handle_noPostsException
     */


    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle_CategoryAlreadyExistsException(CategoryAlreadyExistsException e) {
        log.warn("CategoryAlreadyExistsException, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("name", e.getMessage());

        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoPostsException.class)
    public ResponseEntity<ErrorResponse> handle_NoPostsException(NoPostsException e) {
        log.warn("NoPostsException, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("posts", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoCategoryException.class)
    public ResponseEntity<ErrorResponse> handle_noCategoriesException(NoCategoryException e) {
        log.warn("NoCategories, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("name", e.getMessage());
        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // TODO Create resource not found handler



    /* ============================================================================================================= */



    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handle_BadCredentialsException(BadCredentialsException e) {
        log.warn("BadCredentialsException, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("username", "Username atau password salah");
        errors.put("password", "Username atau password salah");

        ErrorResponse errorResponse = new ErrorResponse(false, "Username atau password salah", errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle_MethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("MethodArgumentNotValidException, message {}", ex.getMessage());
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
     * handle_mailAuthenticationException
     * handle_noResourceFoundException
     * |
     * handle_InternalAuthenticationServiceException
     * handle_AllUncaughtExceptions
     * handle_dataIntegrityViolationException
     */

    @ExceptionHandler({
            DisabledException.class,
            LockedException.class,
    })
    public ResponseEntity<ErrorResponse> handle_AuthenticationExceptions(Exception ex) {
        log.warn("DisabledException || LockedException, message {}", ex.getMessage());
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
        log.warn("MalformedJwtException || SignatureException, message {}", ex.getMessage());
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
        log.warn("ExpiredJwtException, message {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Token kadaluarsa. lakukan refresh token atau login kembali");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handle_AccessDeniedException(AccessDeniedException ex) {
        log.warn("AccessDeniedException, message {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Access Denied. You do not have permission to access this resource.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_ResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("ResourceNotFoundException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handle_PasswordMismatchException(PasswordMismatchException e) {
        log.warn("PasswordMismatchException, message {}", e.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("password_confirm", "Password Tidak Sesuai.");

        ErrorResponse error = new ErrorResponse(false, e.getMessage(), errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessAndRefreshTokenMismatchException.class)
    public ResponseEntity<ErrorResponse> handle_AccessAndRefreshTokenMismatchException(AccessAndRefreshTokenMismatchException e) {
        log.warn("AccessAndRefreshTokenMismatchException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_refreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        log.warn("RefreshTokenNotFoundException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handle_AccessAndRefreshTokenMismatchException(RefreshTokenExpiredException e) {
        log.warn("refreshTokenExpiredException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserIsDisabledException.class)
    public ResponseEntity<ErrorResponse> handle_AccessAndRefreshTokenMismatchException(UserIsDisabledException e) {
        log.warn("UserIsDisabledException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    public ResponseEntity<ErrorResponse> handle_InvalidAccessTokenException(InvalidAccessTokenException e) {
        log.warn("InvalidAccessTokenException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handle_InvalidRefreshTokenException(InvalidRefreshTokenException e) {
        log.warn("InvalidRefreshTokenException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MailAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handle_MailAuthenticationException(MailAuthenticationException e) {
        log.warn("MailAuthenticationException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Email service error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handle_noResourceFoundException(ResourceNotFoundException e) {
        log.warn("handle_noResourceFoundException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Resource Not Found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handle_HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("HttpRequestMethodNotSupportedException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Not such method like that.");
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handle_MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("MethodArgumentTypeMismatchException, message {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Wrong path parameters");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }





















    // =================================================================================================================

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handle_InternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        log.warn("InternalAuthenticationServiceException, message {}", ex.getMessage());
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
        log.warn("AllUncaughtExceptions, message {}", ex.getMessage());
        System.err.println("An unexpected error occurred: " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(false, "An unexpected internal server error occurred. Please try again later.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}