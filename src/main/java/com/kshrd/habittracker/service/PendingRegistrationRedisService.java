package com.kshrd.habittracker.service;

import com.kshrd.habittracker.dto.PendingUserRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PendingRegistrationRedisService {

    private final RedisTemplate<String, PendingUserRegistration> pendingUserRedisTemplate;

    private String emailKey(String email) {
        return "pending_user:email:" + email;
    }

    private String usernameKey(String username) {
        return "pending_user:username:" + username;
    }

    public void savePendingUser(PendingUserRegistration pendingUser, Duration ttl) {
        pendingUserRedisTemplate.opsForValue().set(emailKey(pendingUser.getEmail()), pendingUser, ttl);
        pendingUserRedisTemplate.opsForValue().set(usernameKey(pendingUser.getUsername()), pendingUser, ttl);
    }

    public PendingUserRegistration getPendingUserByEmail(String email) {
        return pendingUserRedisTemplate.opsForValue().get(emailKey(email));
    }

    public PendingUserRegistration getPendingUserByUsername(String username) {
        return pendingUserRedisTemplate.opsForValue().get(usernameKey(username));
    }

    public PendingUserRegistration getPendingUserByIdentifier(String identifier) {
        PendingUserRegistration byEmail = getPendingUserByEmail(identifier);
        if (byEmail != null) {
            return byEmail;
        }
        return getPendingUserByUsername(identifier);
    }

    public void deletePendingUser(PendingUserRegistration pendingUser) {
        if (pendingUser != null) {
            pendingUserRedisTemplate.delete(emailKey(pendingUser.getEmail()));
            pendingUserRedisTemplate.delete(usernameKey(pendingUser.getUsername()));
        }
    }

}