package com.kshrd.habittracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitLog {
    private UUID habitLogId;
    private LocalDate logDate;
    private String status;
    private Integer xpEarned;
    private UUID habitId;
}