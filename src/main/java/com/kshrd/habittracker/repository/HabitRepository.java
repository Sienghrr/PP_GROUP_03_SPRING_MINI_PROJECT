package com.kshrd.habittracker.repository;

import com.kshrd.habittracker.model.AppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.UUID;

@Mapper
public interface HabitRepository {

    @Select("""
                SELECT u.*
                FROM habits h
                JOIN app_users u ON h.app_user_id = u.app_user_id
                WHERE h.habit_id = #{habitId}
            """)
    @Results({
            @Result(property = "appUserId", column = "app_user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "level", column = "level"),
            @Result(property = "xp", column = "xp"),
            @Result(property = "profileImageUrl", column = "profile_image"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "createdAt", column = "created_at")
    })
    AppUser findUserByHabitId(UUID habitId);

}
