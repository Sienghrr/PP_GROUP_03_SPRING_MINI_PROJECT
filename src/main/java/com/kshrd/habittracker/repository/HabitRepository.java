package com.kshrd.habittracker.repository;

import com.kshrd.habittracker.model.AppUser;
import com.kshrd.habittracker.model.Habit;
import org.apache.ibatis.annotations.*;

import java.util.List;
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
            @Result(property = "name", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "level", column = "level"),
            @Result(property = "xp", column = "xp"),
            @Result(property = "profileImageUrl", column = "profile_image"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "createdAt", column = "created_at")
    })
    AppUser findUserByHabitId(UUID habitId);

    @Results(id = "habitMapper", value = {
            @Result(property = "habitId", column = "habit_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "frequency", column = "frequency"),
            @Result(property = "isActive", column = "is_active"),
            @Result(property = "appUserId", column = "app_user_id"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Select("""
        SELECT * FROM habits
        WHERE app_user_id = #{appUserId}
        ORDER BY created_at DESC
        LIMIT #{size}
        OFFSET #{size} * (#{page}-1)
    """)
    List<Habit> findAllByUserId(UUID appUserId,Integer page, Integer size);

    @ResultMap("habitMapper")
    @Select("""
        SELECT * FROM habits
        WHERE habit_id = #{habitId}
          AND app_user_id = #{appUserId}
    """)
    Habit findByIdAndUserId(@Param("habitId") UUID habitId,
                            @Param("appUserId") UUID appUserId);

    @ResultMap("habitMapper")
    @Select("""
        INSERT INTO habits (title, description, frequency, is_active, app_user_id)
        VALUES (#{title}, #{description}, #{frequency}, #{isActive}, #{appUserId})
        RETURNING *
    """)
    Habit save(Habit habit);

    @Update("""
        UPDATE habits
        SET title = #{title},
            description = #{description},
            frequency = #{frequency},
            is_active = #{isActive}
        WHERE habit_id = #{habitId}
          AND app_user_id = #{appUserId}
    """)
    int update(@Param("habitId") UUID habitId,
               @Param("appUserId") UUID appUserId,
               @Param("title") String title,
               @Param("description") String description,
               @Param("frequency") String frequency,
               @Param("isActive") Boolean isActive);

    @Delete("""
        DELETE FROM habits
        WHERE habit_id = #{habitId}
          AND app_user_id = #{appUserId}
    """)
    int delete(@Param("habitId") UUID habitId,
               @Param("appUserId") UUID appUserId);

}
