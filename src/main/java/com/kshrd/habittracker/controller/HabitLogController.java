package com.kshrd.habittracker.controller;


import com.kshrd.habittracker.dto.request.habitlog.CreateHabitLogRequest;
import com.kshrd.habittracker.dto.response.ApiResponse;
import com.kshrd.habittracker.model.HabitLog;
import com.kshrd.habittracker.service.HabitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/habit-log/")
@RequiredArgsConstructor
@Validated
public class HabitLogController {

    private final HabitLogService habitLogService;

    @PostMapping
    public ResponseEntity<ApiResponse<HabitLog>> createHabitLog(
            @RequestBody CreateHabitLogRequest request) {

        HabitLog habitLog = habitLogService.createHabitLog(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(habitLog, "CREATED",HttpStatus.CREATED)
        );
    }

    @GetMapping("/{habitLogId}")
    public ResponseEntity<ApiResponse<HabitLog>> getHabitLogById(@PathVariable UUID habitLogId){
        return null;
    }
}
