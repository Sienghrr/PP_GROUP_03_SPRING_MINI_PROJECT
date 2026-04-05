package com.kshrd.habittracker.service;

import com.kshrd.habittracker.model.Achievement;

import java.util.List;
import java.util.UUID;

public interface AchievementService {


    List<Achievement> getAllAchievements(Integer page, Integer size);
}
