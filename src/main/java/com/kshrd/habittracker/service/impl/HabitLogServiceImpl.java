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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitLogServiceImpl implements HabitLogService {
    private final HabitLogRepository habitLogRepository;
    private final HabitRepository habitRepository;
    private final AppUserRepository userRepository;

    @Override
    @Transactional
    public HabitLog createHabitLog(CreateHabitLogRequest request) {


        if (request.getHabitId() == null) {
            throw new IllegalArgumentException("habitId required");
        }

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            throw new IllegalArgumentException("status required");
        }

        int xp = calculateXP(request.getStatus());


        HabitLog log = habitLogRepository.insertHabitLog(
                request.getHabitId(),
                request.getStatus(),
                xp
        );

        if (log == null) {
            throw new RuntimeException("Insert habit log failed");
        }

        AppUser user = habitRepository.findUserByHabitId(request.getHabitId());

        if (user == null) {
            throw new RuntimeException("User not found for this habit");
        }

        int newXp = user.getXp() + xp;
        int newLevel = newXp / 100;

        userRepository.updateUserXpAndLevel(
                user.getAppUserId(),
                newXp,
                newLevel
        );

        return habitLogRepository.findById(log.getHabitLogId());
    }

    @Override
    public HabitLog getHabitLogById(UUID habitLogId) {

        HabitLog habitLog = habitLogRepository.findById(habitLogId);

        if (habitLog == null) {
            throw new IllegalArgumentException("HabitLog not found");
        }

        return habitLog;
    }

    private int calculateXP(String status) {
        return switch (status.toUpperCase()) {
            case "COMPLETED" -> 10;
            case "SKIPPED" -> 2;
            case "FAILED" -> 0;
            default -> throw new IllegalArgumentException("Invalid status");
        };
    }
}
