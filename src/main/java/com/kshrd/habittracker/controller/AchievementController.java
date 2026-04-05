package com.kshrd.habittracker.controller;

import com.kshrd.habittracker.dto.response.ApiResponse;
import com.kshrd.habittracker.model.Achievement;
import com.kshrd.habittracker.service.AchievementService;
import com.kshrd.habittracker.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AchievementController {

    private final AchievementService achievementService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<Achievement>>> getAllAchievements(
            @RequestParam(required = false , defaultValue = "1") Integer page,
            @RequestParam(required = false , defaultValue = "10") Integer size

    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        achievementService.getAllAchievements(page,size),
                        "Achievements retrieved successfully!",
                        HttpStatus.OK
                )
        );
    }



}