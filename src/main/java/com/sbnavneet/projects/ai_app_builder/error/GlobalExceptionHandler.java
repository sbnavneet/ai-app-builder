package com.sbnavneet.projects.ai_app_builder.error;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleBadRequest(ResourceNotFoundException ex){
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getResourceName() + " with Id " + ex.getResourceId() + " not found." );
        return ResponseEntity.status(error.status()).body(error);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex){
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBadRequest(MethodArgumentNotValidException ex){
        List<ApiFieldError> errors = ex.getBindingResult().getFieldErrors().stream().map(e -> new ApiFieldError(e.getField(), e.getDefaultMessage())).toList();
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Input Validation Failed", errors);
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException ex){
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex){
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, "Invalid token: " + ex.getLocalizedMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex){
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex){
        ex.printStackTrace();
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }
}
