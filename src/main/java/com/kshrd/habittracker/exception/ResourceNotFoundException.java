package com.kshrd.habittracker.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName, UUID id) {
        super("%s with id = %s not found".formatted(resourceName,id) );
        HttpStatus status = HttpStatus.NOT_FOUND;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}