package com.kshrd.habittracker.utils;

import com.kshrd.habittracker.model.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}