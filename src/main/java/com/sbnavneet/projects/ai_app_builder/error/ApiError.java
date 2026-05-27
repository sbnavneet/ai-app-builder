package com.sbnavneet.projects.ai_app_builder.error;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;


public record ApiError(
    HttpStatus status,
    String message,
    Instant timeStamp,
    @JsonInclude(JsonInclude.Include.NON_NULL) List<ApiFieldError> errorList
) {
    public ApiError(HttpStatus status, String message){
        this(status, message, Instant.now(), null);
    }
    public ApiError(HttpStatus status, String message, List<ApiFieldError> errorList){
        this(status, message,Instant.now(),  errorList );
    }
}


record ApiFieldError(String field , String message){}