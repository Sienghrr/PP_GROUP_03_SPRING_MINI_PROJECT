package com.kshrd.habittracker.dto.response.habitlog;

import com.kshrd.habittracker.model.Habit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitLogResponse {
    private UUID habitLogId;
    private LocalDate logDate;
    private String status;
    private Integer xpEarned;
    private Habit habit;
}