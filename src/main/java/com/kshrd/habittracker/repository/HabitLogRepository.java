package com.kshrd.habittracker.repository;

import com.kshrd.habittracker.model.HabitLog;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface HabitLogRepository {

    @Results(id = "habitLogMapper", value = {
            @Result(property = "habitLogId", column = "habit_log_id"),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "status", column = "status"),
            @Result(property = "xpEarned", column = "xp_earned"),

            @Result(property = "habit", column = "habit_id",
                    one = @One(select = "com.kshrd.habittracker.repository.HabitRepository.findHabitById")
            )
    })

    @Select("""
        SELECT
            habit_log_id,
            log_date,
            status,
            xp_earned,
            habit_id
        FROM habit_logs
        WHERE habit_log_id = #{id}
    """)
    HabitLog findById(@Param("id") UUID id);

}
