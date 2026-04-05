package com.kshrd.habittracker.service.impl;

import com.kshrd.habittracker.dto.request.habitlog.CreateHabitLogRequest;
import com.kshrd.habittracker.model.AppUser;
import com.kshrd.habittracker.model.HabitLog;
import com.kshrd.habittracker.repository.AppUserRepository;
import com.kshrd.habittracker.repository.HabitLogRepository;
import com.kshrd.habittracker.repository.HabitRepository;
import com.kshrd.habittracker.service.HabitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor

public class HabitLogServiceImpl implements HabitLogService {

    private final HabitLogRepository habitLogRepository;
    private final HabitRepository habitRepository;
    private final AppUserRepository userRepository;


    @Override
    public HabitLog createHabitLog(CreateHabitLogRequest request) {
/*

        if (request.getHabitId() == null) {
            throw new IllegalArgumentException("habitId must not be null");
        }

        if (request.getLogDate() == null) {
            request.setLogDate(LocalDate.now());
        }

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            throw new IllegalArgumentException("status must not be empty");
        }

        boolean exists = habitLogRepository.existsByHabitIdAndDate(
                request.getHabitId(),
                request.getLogDate()
        );

        if (exists) {
            throw new IllegalArgumentException("Habit already logged for this date");
        }

        // 5. Calculate XP (fixed = 10 for DONE)
        int xp = calculateXP(request.getStatus());
        request.setXpEarned(xp);

        // 6. Insert habit log
        HabitLog habitLog = habitLogRepository.insertHabitLog(request);

        // 🔥 7. Update user XP + level
        UUID userId = habitRepository.findUserIdByHabitId(request.getHabitId());

        AppUser user = userRepository.findById(userId);

        int newXp = user.getXp() + xp;
        int newLevel = user.getLevel();

        // level up logic
        while (newXp >= 100) {
            newXp -= 100;
            newLevel++;
        }

        userRepository.updateUserXpAndLevel(userId, newXp, newLevel);

        return habitLog;
*/
        return null;
    }



    private int calculateXP(String status) {
        return switch (status.toUpperCase()) {
            case "DONE" -> 10;
            case "SKIPPED" -> 2;
            case "FAILED" -> 0;
            default -> throw new IllegalArgumentException("Invalid status");
        };
    }


    @Override
    public HabitLog getHabitLogById(UUID habitLogId) {
        HabitLog habitLog = habitLogRepository.findById(habitLogId);

        if (habitLog.getHabitLogId() != habitLogId) {
            return null;
        }


        return null;
    }
}
