package com.kshrd.habittracker.dto.request.habit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateHabitRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String frequency;

    @NotNull
    private Boolean isActive;
}