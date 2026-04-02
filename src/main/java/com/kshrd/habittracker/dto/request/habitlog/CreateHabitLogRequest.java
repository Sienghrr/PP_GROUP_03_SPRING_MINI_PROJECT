package com.kshrd.habittracker.dto.request.habitlog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateHabitLogRequest {
    @NotNull
    private LocalDate logDate;

    @NotBlank
    private String status;
}