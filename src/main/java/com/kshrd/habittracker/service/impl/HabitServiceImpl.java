package com.kshrd.habittracker.service.impl;

import com.kshrd.habittracker.dto.request.habit.CreateHabitRequest;
import com.kshrd.habittracker.dto.request.habit.UpdateHabitRequest;
import com.kshrd.habittracker.exception.BadRequestException;
import com.kshrd.habittracker.exception.ResourceNotFoundException;
import com.kshrd.habittracker.model.Habit;
import com.kshrd.habittracker.repository.HabitRepository;
import com.kshrd.habittracker.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;

    @Cacheable(value = "habitListCache", key = "#appUserId")
    public List<Habit> getAllHabits(UUID appUserId,Integer page, Integer size) {
        return habitRepository.findAllByUserId(appUserId,page,size);
    }

    @Cacheable(value = "habitCache", key = "#habitId + ':' + #appUserId")
    public Habit getHabitById(UUID appUserId, UUID habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, appUserId);
        if (habit == null) {
            throw new ResourceNotFoundException("Habit not found");
        }
        return habit;
    }

    @Caching(evict = {
            @CacheEvict(value = "habitListCache", key = "#appUserId")
    })
    public Habit createHabit(UUID appUserId, CreateHabitRequest request) {
        Habit habit = new Habit();
        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setFrequency(request.getFrequency());
        habit.setIsActive(true);
        habit.setAppUserId(appUserId);

        return habitRepository.save(habit);
    }

    @Caching(evict = {
            @CacheEvict(value = "habitCache", key = "#habitId + ':' + #appUserId"),
            @CacheEvict(value = "habitListCache", key = "#appUserId")
    })
    public Habit updateHabit(UUID appUserId, UUID habitId, UpdateHabitRequest request) {
        Habit existing = habitRepository.findByIdAndUserId(habitId, appUserId);
        if (existing == null) {
            throw new ResourceNotFoundException("Habit not found");
        }

        int updated = habitRepository.update(
                habitId,
                appUserId,
                request.getTitle(),
                request.getDescription(),
                request.getFrequency(),
                request.getIsActive()
        );

        if (updated == 0) {
            throw new BadRequestException("Failed to update habit");
        }

        return habitRepository.findByIdAndUserId(habitId, appUserId);
    }

    @Caching(evict = {
            @CacheEvict(value = "habitCache", key = "#habitId + ':' + #appUserId"),
            @CacheEvict(value = "habitListCache", key = "#appUserId")
    })
    public void deleteHabit(UUID appUserId, UUID habitId) {
        int deleted = habitRepository.delete(habitId, appUserId);
        if (deleted == 0) {
            throw new ResourceNotFoundException("Habit not found");
        }
    }
}
