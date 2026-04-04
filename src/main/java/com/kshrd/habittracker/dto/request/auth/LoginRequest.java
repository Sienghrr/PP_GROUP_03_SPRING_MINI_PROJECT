package com.kshrd.habittracker.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "identifier cannot be blank")
    private String identifier;

    @NotBlank(message = "identifier cannot be blank")
    private String password;
}