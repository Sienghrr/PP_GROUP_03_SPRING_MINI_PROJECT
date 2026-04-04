package com.kshrd.habittracker.controller;

import com.kshrd.habittracker.dto.request.profile.UpdateProfileRequest;
import com.kshrd.habittracker.dto.response.ApiResponse;
import com.kshrd.habittracker.dto.response.appuser.AppUserResponse;
import com.kshrd.habittracker.model.AppUser;
import com.kshrd.habittracker.service.ProfileService;
import com.kshrd.habittracker.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<AppUserResponse>> getUserProfile() {
        AppUser currentUser = SecurityUtils.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        profileService.getProfile(currentUser.getAppUserId()),
                        "Profile retrieved successfully",
                        HttpStatus.OK
        )
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AppUserResponse>> updateUserProfile(@Valid @RequestBody UpdateProfileRequest request) {
        AppUser currentUser = SecurityUtils.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        profileService.updateProfile(currentUser.getAppUserId(), request),
                        "Profile updated successfully",
                        HttpStatus.OK
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUserProfile() {
        profileService.deleteProfile( SecurityUtils.getCurrentUser().getAppUserId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Profile deleted successfully",
                        HttpStatus.OK
                )
        );
    }
}