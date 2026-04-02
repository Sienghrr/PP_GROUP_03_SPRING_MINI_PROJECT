package com.kshrd.habittracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Achievement {
    private UUID achievementId;
    private String title;
    private String description;
    private String badge;
    private Integer xpRequired;
}