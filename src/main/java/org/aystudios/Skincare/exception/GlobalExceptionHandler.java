package org.aystudios.Skincare.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.aystudios.Skincare.dto.ApiErrorResponse;
import org.aystudios.Skincare.exception.auth.*;
import org.aystudios.Skincare.exception.general.ResourceNotFoundException;
import org.aystudios.Skincare.exception.product.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===================== HELPER METHODS=====================

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String code, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(status.value(), code, message, req.getRequestURI(), LocalDateTime.now()));
    }


    // ===================== AUTH =====================

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(UserAlreadyExistedException.class)
    public ResponseEntity<ApiErrorResponse> handleUserExists(
            UserAlreadyExistedException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS", ex.getMessage(), req);
    }

    @ExceptionHandler({InvalidPasswordException.class, InvalidTokenException.class, TokenExpiredException.class})
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(
            RuntimeException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), req);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(
            UnauthorizedActionException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage(), req);
    }


    // ===================== PRODUCT =====================

    @ExceptionHandler({ProductNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            RuntimeException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage(), req);
    }


    // ===================== SPRING / FRAMEWORK =====================

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEndpointNotFound(
            HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "ENDPOINT_NOT_FOUND", "API endpoint does not exist", req);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(
                HttpStatus.METHOD_NOT_ALLOWED,
                "METHOD_NOT_ALLOWED",
                ex.getMessage(),
                req
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationRequired(
            AuthenticationException ex, HttpServletRequest req) {
        return build(
                HttpStatus.UNAUTHORIZED,
                "AUTH_REQUIRED",
                "Authentication required",
                req
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest req) {
        return build(
                HttpStatus.FORBIDDEN,
                "ACCESS_DENIED",
                "You do not have permission to access this resource",
                req
        );
    }


}

