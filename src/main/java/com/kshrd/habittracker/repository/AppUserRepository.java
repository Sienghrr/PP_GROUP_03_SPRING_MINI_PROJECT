package com.kshrd.habittracker.repository;

import com.kshrd.habittracker.model.AppUser;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface AppUserRepository {

    @Results(id = "appUserMapper", value = {
            @Result(property = "appUserId", column = "app_user_id"),
            @Result(property = "name", column = "username"),
            @Result(property = "profileImageUrl", column = "profile_image"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Select("""
            SELECT app_user_id, username, email, password, level, xp, profile_image, is_verified, created_at
            FROM app_users
            WHERE app_user_id = #{appUserId}
            """)
    AppUser findById(@Param("appUserId") UUID appUserId);

    @ResultMap("appUserMapper")
    @Select("""
            SELECT app_user_id, username, email, password, level, xp, profile_image, is_verified, created_at
            FROM app_users
            WHERE email = #{identifier} OR username = #{identifier}
            """)
    AppUser findByEmailOrUsername(@Param("identifier") String identifier);

    @ResultMap("appUserMapper")
    @Select("""
            SELECT app_user_id, username, email, password, level, xp, profile_image, is_verified, created_at
            FROM app_users
            WHERE email = #{email} 
            """)
    AppUser findByEmail(@Param("email") String email);

    @ResultMap("appUserMapper")
    @Select("""
            SELECT app_user_id, username, email, password, level, xp, profile_image, is_verified, created_at
            FROM app_users
            WHERE username = #{username} 
            """)
    AppUser findByUsername(@Param("username") String username);

    @ResultMap("appUserMapper")
    @Select("""
            INSERT INTO app_users (
                username, email, password, level, xp, profile_image, is_verified
            )
            VALUES (
                #{name}, #{email}, #{password}, #{level}, #{xp}, #{profileImageUrl}, #{isVerified}
            )
            RETURNING *
            """)
    AppUser save(AppUser appUser);

    @Update("""
            UPDATE app_users
            SET username = #{username},
                profile_image = #{profileImage}
            WHERE app_user_id = #{appUserId}
            """)
    int updateProfile(@Param("appUserId") UUID appUserId,
                      @Param("username") String username,
                      @Param("profileImage") String profileImage);



    @Delete("""
            DELETE FROM app_users
            WHERE app_user_id = #{appUserId}
            """)
    int deleteById(@Param("appUserId") UUID appUserId);
}