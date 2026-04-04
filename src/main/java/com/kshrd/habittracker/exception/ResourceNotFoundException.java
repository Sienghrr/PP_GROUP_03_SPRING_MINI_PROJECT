package com.kshrd.habittracker.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    private HttpStatus status;
    public ResourceNotFoundException(String resourceName, Long id) {
        super("%s with id = %d not found".formatted(resourceName,id) );
        this.status = HttpStatus.NOT_FOUND;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}