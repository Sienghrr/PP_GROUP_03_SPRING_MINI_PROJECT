package com.kshrd.habittracker.service.impl;

import com.kshrd.habittracker.model.Achievement;
import com.kshrd.habittracker.repository.AchievementRepository;
import com.kshrd.habittracker.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    @Cacheable(value = "achievementListCache")
    @Override
    public List<Achievement> getAllAchievements(Integer page, Integer size) {
        return achievementRepository.findAll(page,size);
    }
}
