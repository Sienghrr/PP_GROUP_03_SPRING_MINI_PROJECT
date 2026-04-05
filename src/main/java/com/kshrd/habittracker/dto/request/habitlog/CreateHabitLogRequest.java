package com.kshrd.habittracker.dto.request.habitlog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHabitLogRequest {
    @NotNull
    private UUID habitId;

    @NotBlank
    private String status;
}