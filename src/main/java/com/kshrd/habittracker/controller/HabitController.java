package com.kshrd.habittracker.controller;

import com.kshrd.habittracker.dto.request.habit.CreateHabitRequest;
import com.kshrd.habittracker.dto.request.habit.UpdateHabitRequest;
import com.kshrd.habittracker.dto.response.ApiResponse;
import com.kshrd.habittracker.model.Habit;
import com.kshrd.habittracker.service.HabitService;
import com.kshrd.habittracker.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/habits")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class HabitController {

    private final HabitService habitService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Habit>>> getAllHabits(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        habitService.getAllHabits(SecurityUtils.getCurrentUser().getAppUserId(), page, size),
                        "Habits retrieved successfully",
                        HttpStatus.OK
                )

        );
    }

    @GetMapping("/{habitId}")
    public ResponseEntity<ApiResponse<Habit>> getHabitById(@PathVariable UUID habitId) {
        return ResponseEntity.ok(
                ApiResponse
                        .success(
                                habitService.getHabitById(SecurityUtils.getCurrentUser().getAppUserId(), habitId),
                                "Habit retrieved successfully",
                                HttpStatus.OK
                        )

        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Habit>> createHabit(@Valid @RequestBody CreateHabitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse
                        .success(
                                habitService.createHabit(SecurityUtils.getCurrentUser().getAppUserId(), request),
                                "Habit created successfully",
                                HttpStatus.CREATED
                        )

        );
    }

    @PutMapping("/{habitId}")
    public ResponseEntity<ApiResponse<Habit>> updateHabit(@PathVariable UUID habitId,
                                                          @Valid @RequestBody UpdateHabitRequest request) {
        return ResponseEntity.ok(
                ApiResponse
                        .success(habitService.updateHabit(SecurityUtils.getCurrentUser().getAppUserId(), habitId, request),
                                "Habit updated successfully",
                                HttpStatus.OK
                        )
        );

    }

    @DeleteMapping("/{habitId}")
    public ResponseEntity<ApiResponse<Void>> deleteHabit(@PathVariable UUID habitId) {
        habitService.deleteHabit(SecurityUtils.getCurrentUser().getAppUserId(), habitId);

        return ResponseEntity.ok(
                ApiResponse
                        .success(null,
                                "Habit deleted successfully",
                                HttpStatus.OK)
        );
    }
}
