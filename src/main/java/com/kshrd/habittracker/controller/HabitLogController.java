package com.kshrd.habittracker.controller;

import com.kshrd.habittracker.dto.request.habitlog.CreateHabitLogRequest;
import com.kshrd.habittracker.dto.response.ApiResponse;
import com.kshrd.habittracker.model.HabitLog;
import com.kshrd.habittracker.service.HabitLogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/habit-log/")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class HabitLogController {

    private final HabitLogService habitLogService;

    @PostMapping
    public ResponseEntity<ApiResponse<HabitLog>> createHabitLog(
            @RequestBody CreateHabitLogRequest request
    ) {

        HabitLog response = habitLogService.createHabitLog(request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Create success", HttpStatus.OK)
        );
    }

    @GetMapping("/{habitLogId}")
    public ResponseEntity<ApiResponse<HabitLog>> getHabitLogById(
            @PathVariable UUID habitLogId
    ) {

        HabitLog habitLog = habitLogService.getHabitLogById(habitLogId);

        return ResponseEntity.ok(
                ApiResponse.success(habitLog, "Get habit log success", HttpStatus.OK)
        );
    }
}
