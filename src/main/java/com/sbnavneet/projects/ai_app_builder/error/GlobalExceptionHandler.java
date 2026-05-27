package com.sbnavneet.projects.ai_app_builder.error;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
