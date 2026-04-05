package com.kshrd.habittracker.service;

import com.kshrd.habittracker.dto.request.habit.CreateHabitRequest;
import com.kshrd.habittracker.dto.request.habit.UpdateHabitRequest;
import com.kshrd.habittracker.model.Habit;

import java.util.List;
import java.util.UUID;

public interface HabitService {
    List<Habit> getAllHabits(UUID appUserId,Integer page, Integer size);
    Habit getHabitById(UUID appUserId, UUID habitId);
    Habit createHabit(UUID appUserId, CreateHabitRequest request);
    Habit updateHabit(UUID appUserId, UUID habitId, UpdateHabitRequest request);
    void deleteHabit(UUID appUserId, UUID habitId);
}

