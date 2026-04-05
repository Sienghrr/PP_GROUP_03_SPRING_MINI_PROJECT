package com.kshrd.habittracker.repository;

import com.kshrd.habittracker.model.Achievement;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AchievementRepository {

    @Results(id = "achievementMapper", value = {
            @Result(property = "achievementId", column = "achievement_id"),
            @Result(property = "xpRequired", column = "xp_required")
    })
    @Select("""
                SELECT * FROM achievements
                ORDER BY xp_required ASC
                LIMIT #{size}
                OFFSET (#{page}-1) * #{size}
            """)
    List<Achievement> findAll(Integer page, Integer size);

    @ResultMap("achievementMapper")
    @Select("""
                SELECT a.*
                FROM achievements a
                JOIN app_user_achievements aua ON a.achievement_id = aua.achievement_id
                WHERE aua.app_user_id = #{appUserId}
                ORDER BY a.xp_required ASC
                LIMIT #{size}
                OFFSET (#{page}-1) * #{size}
            """)
    List<Achievement> findAllByUserId(UUID appUserId, Integer page, Integer size);


}
