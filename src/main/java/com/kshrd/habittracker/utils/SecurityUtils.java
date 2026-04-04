package com.kshrd.habittracker.utils;

import com.kshrd.habittracker.exception.UnauthorizedException;
import com.kshrd.habittracker.model.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    public static String getCurrentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    public static AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static UUID getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UUID uuid) {
            return uuid;
        }
        throw new UnauthorizedException("Unauthorized");
    }
}