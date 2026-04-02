package com.kshrd.habittracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private UUID appUserId;
    private String username;
    private String email;
}