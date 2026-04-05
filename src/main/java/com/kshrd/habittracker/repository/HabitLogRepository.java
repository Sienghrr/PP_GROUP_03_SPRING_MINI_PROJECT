package com.kshrd.habittracker.repository;

import com.kshrd.habittracker.dto.response.habitlog.HabitLogResponse;
import com.kshrd.habittracker.model.HabitLog;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface HabitLogRepository {
    @Select("""
            INSERT INTO habit_logs (habit_id, status, xp_earned, log_date)
            VALUES (#{habitId}, #{status}, #{xp}, CURRENT_DATE)
            RETURNING habit_log_id, log_date, status, xp_earned, habit_id
            """)
    HabitLog insertHabitLog(
            @Param("habitId") UUID habitId,
            @Param("status") String status,
            @Param("xp") int xp
    );

    @Select("""
            SELECT 
                habit_log_id,
                log_date,
                status,
                xp_earned,
                habit_id
            FROM habit_logs
            WHERE habit_log_id = #{habitLogId}
            LIMIT 1
            """)
    @Results(id = "habitLogMapper", value = {
            @Result(property = "habitLogId", column = "habit_log_id"),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "status", column = "status"),
            @Result(property = "xpEarned", column = "xp_earned"),
            @Result(property = "habitId", column = "habit_id")
    })
    HabitLog findById(UUID habitLogId);

    @Select("""
            SELECT 
                hl.habit_log_id,
                hl.log_date,
                hl.status,
                hl.xp_earned,
            
                h.habit_id,
                h.title,
                h.description,
                h.frequency,
                h.is_active,
                h.created_at AS habit_created_at,
            
                u.app_user_id,
                u.username,
                u.email,
                u.level,
                u.xp,
                u.profile_image,
                u.is_verified,
                u.created_at AS user_created_at
            
            FROM habit_logs hl
            JOIN habits h ON hl.habit_id = h.habit_id
            JOIN app_users u ON h.app_user_id = u.app_user_id
            
            WHERE hl.habit_log_id = #{habitLogId}
            LIMIT 1
            """)
    @Results(id = "habitLogDtoMapper", value = {

            @Result(property = "habitLogId", column = "habit_log_id"),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "status", column = "status"),
            @Result(property = "xpEarned", column = "xp_earned"),

            @Result(property = "habit.habitId", column = "habit_id"),
            @Result(property = "habit.title", column = "title"),
            @Result(property = "habit.description", column = "description"),
            @Result(property = "habit.frequency", column = "frequency"),
            @Result(property = "habit.isActive", column = "is_active"),
            @Result(property = "habit.createdAt", column = "habit_created_at"),

            @Result(property = "habit.appUserResponse.appUserId", column = "app_user_id"),
            @Result(property = "habit.appUserResponse.username", column = "username"),
            @Result(property = "habit.appUserResponse.email", column = "email"),
            @Result(property = "habit.appUserResponse.level", column = "level"),
            @Result(property = "habit.appUserResponse.xp", column = "xp"),
            @Result(property = "habit.appUserResponse.profileImageUrl", column = "profile_image"),
            @Result(property = "habit.appUserResponse.isVerified", column = "is_verified"),
            @Result(property = "habit.appUserResponse.createdAt", column = "user_created_at")
    })
    HabitLogResponse findDetailById(UUID habitLogId);
}
