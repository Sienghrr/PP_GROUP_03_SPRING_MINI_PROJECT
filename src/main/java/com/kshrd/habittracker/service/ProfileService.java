package com.kshrd.habittracker.service;

import com.kshrd.habittracker.dto.request.profile.UpdateProfileRequest;
import com.kshrd.habittracker.dto.response.appuser.AppUserResponse;
import com.kshrd.habittracker.exception.BadRequestException;
import com.kshrd.habittracker.exception.ResourceNotFoundException;
import com.kshrd.habittracker.model.AppUser;
import com.kshrd.habittracker.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AppUserRepository appUserRepository;

    @Cacheable(value = "profileCache", key = "#appUserId")
    public AppUserResponse getProfile(UUID appUserId) {
        AppUser user = appUserRepository.findById(appUserId);

        if (user == null) {
            throw new ResourceNotFoundException("Profile not found");
        }

        return AppUserResponse.builder()
                .appUserId(user.getAppUserId())
                .username(user.getName())
                .email(user.getEmail())
                .level(user.getLevel())
                .xp(user.getXp())
                .profileImageUrl(user.getProfileImageUrl())
                .isVerified(user.getIsVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @CacheEvict(value = "profileCache", key = "#appUserId")
    public AppUserResponse updateProfile(UUID appUserId, UpdateProfileRequest request) {
        AppUser existing = appUserRepository.findById(appUserId);
        if (existing == null) {
            throw new ResourceNotFoundException("Profile not found");
        }

        int updated = appUserRepository.updateProfile(appUserId, request.getUsername(), request.getProfileImageUrl());
        if (updated == 0) {
            throw new BadRequestException("Failed to update profile");
        }

        AppUser user = appUserRepository.findById(appUserId);

        return AppUserResponse.builder()
                .appUserId(user.getAppUserId())
                .username(user.getName())
                .email(user.getEmail())
                .level(user.getLevel())
                .xp(user.getXp())
                .profileImageUrl(user.getProfileImageUrl())
                .isVerified(user.getIsVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @CacheEvict(value = "profileCache", key = "#appUserId")
    public void deleteProfile(UUID appUserId) {
        int deleted = appUserRepository.deleteById(appUserId);
        if (deleted == 0) {
            throw new ResourceNotFoundException("Profile not found");
        }
    }
}