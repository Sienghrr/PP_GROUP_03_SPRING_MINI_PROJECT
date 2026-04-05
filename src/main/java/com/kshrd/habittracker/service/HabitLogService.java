package com.kshrd.habittracker.service;

import com.kshrd.habittracker.dto.request.habitlog.CreateHabitLogRequest;
import com.kshrd.habittracker.model.HabitLog;

import java.util.UUID;

public interface HabitLogService {
    HabitLog createHabitLog(CreateHabitLogRequest habitLogRequest);
    HabitLog getHabitLogById(UUID habitLogId);
}
