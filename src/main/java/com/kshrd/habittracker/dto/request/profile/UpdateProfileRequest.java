package com.kshrd.habittracker.dto.request.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank(message = "username cannot be blank")
    private String username;

    private String profileImageUrl;
}