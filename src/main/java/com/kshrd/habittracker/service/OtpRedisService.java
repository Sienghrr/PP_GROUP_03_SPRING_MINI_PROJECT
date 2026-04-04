package com.kshrd.habittracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveOtp(String email, String otp, Duration ttl) {
        redisTemplate.opsForValue().set("otp:" + email, otp, ttl);
    }

    public String getOtp(String email) {
        Object value = redisTemplate.opsForValue().get("otp:" + email);
        return value == null ? null : value.toString();
    }

    public void deleteOtp(String email) {
        redisTemplate.delete("otp:" + email);
    }

    public void setResendCooldown(String email, Duration ttl) {
        redisTemplate.opsForValue().set("otp_resend:" + email, "LOCKED", ttl);
    }

    public boolean hasResendCooldown(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("otp_resend:" + email));
    }
}