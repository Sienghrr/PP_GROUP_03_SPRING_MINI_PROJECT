package com.kshrd.habittracker.dto.request.habit;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateHabitRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String frequency;
}