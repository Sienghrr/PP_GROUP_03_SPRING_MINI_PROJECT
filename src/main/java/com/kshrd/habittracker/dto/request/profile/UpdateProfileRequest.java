package com.kshrd.habittracker.dto.request.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank
    private String username;

    private String profileImageUrl;
}