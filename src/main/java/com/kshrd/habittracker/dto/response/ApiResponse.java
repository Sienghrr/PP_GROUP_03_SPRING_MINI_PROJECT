package com.kshrd.habittracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    private boolean success;
    private String message;
    private HttpStatus status;
    private T payload;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(T payload, String message,HttpStatus status){
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .payload(payload)
                .timestamp(Instant.now())
                .build();
    }

}