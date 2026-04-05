package com.kshrd.habittracker.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName, Long id) {
        super("%s with id = %d not found".formatted(resourceName,id) );
        HttpStatus status = HttpStatus.NOT_FOUND;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}