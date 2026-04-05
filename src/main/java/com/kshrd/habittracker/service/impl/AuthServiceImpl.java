package com.kshrd.habittracker.service.impl;

import com.kshrd.habittracker.config.jwt.JwtUtils;
import com.kshrd.habittracker.dto.PendingUserRegistration;
import com.kshrd.habittracker.dto.request.auth.LoginRequest;
import com.kshrd.habittracker.dto.request.auth.RegisterRequest;
import com.kshrd.habittracker.dto.response.AuthResponse;
import com.kshrd.habittracker.dto.response.appuser.AppUserResponse;
import com.kshrd.habittracker.exception.BadRequestException;
import com.kshrd.habittracker.exception.ResourceNotFoundException;
import com.kshrd.habittracker.exception.TooManyRequestsException;
import com.kshrd.habittracker.exception.UnauthorizedException;
import com.kshrd.habittracker.model.AppUser;
import com.kshrd.habittracker.repository.AppUserRepository;
import com.kshrd.habittracker.service.*;
import com.kshrd.habittracker.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final OtpRedisService otpRedisService;
    private final MailService mailService;
    private final RateLimitService rateLimitService;
    private final PendingRegistrationRedisService pendingRegistrationRedisService;

    @Value("${app.otp.ttl-minutes}")
    private long otpTtlMinutes;

    @Value("${app.otp.resend-cooldown-seconds}")
    private long resendCooldownSeconds;

    @Value("${app.otp.verify-max-attempts}")
    private int verifyMaxAttempts;

    @Value("${app.otp.verify-window-minutes}")
    private long verifyWindowMinutes;

    @Override
    public AppUserResponse register(RegisterRequest request) {
        AppUser existingUsername = appUserRepository.findByUsername(request.getUsername());
        if (existingUsername != null) {
            throw new BadRequestException("Username already exists");
        }

        AppUser existingEmail = appUserRepository.findByEmail(request.getEmail());
        if (existingEmail != null) {
            throw new BadRequestException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        PendingUserRegistration pendingUser = PendingUserRegistration.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .profileImageUrl(request.getProfileUrlImage())
                .build();

        pendingRegistrationRedisService.savePendingUser(
                pendingUser,
                Duration.ofMinutes(otpTtlMinutes)
        );

        String otp = OtpUtil.generateSixDigitOtp();
        otpRedisService.saveOtp(request.getEmail(), otp, Duration.ofMinutes(otpTtlMinutes));
        otpRedisService.setResendCooldown(request.getEmail(), Duration.ofSeconds(resendCooldownSeconds));
        mailService.sendOtpVerificationEmail(request.getEmail(), request.getUsername(), otp);

        return AppUserResponse.builder()
                .appUserId(UUID.randomUUID())
                .username(request.getUsername())
                .email(request.getEmail())
                .level(1)
                .xp(0)
                .profileImageUrl(request.getProfileUrlImage())
                .isVerified(false)
                .createdAt(Instant.now())
                .build();
    }

    @Override
    public void verify(String email, String otp) {
        String rateKey = "rate_limit:verify:" + email;
        boolean allowed = rateLimitService.isAllowed(rateKey, verifyMaxAttempts, Duration.ofMinutes(verifyWindowMinutes));
        if (!allowed) {
            throw new TooManyRequestsException("Too many OTP verification attempts");
        }

        String savedOtp = otpRedisService.getOtp(email);
        if (savedOtp == null) {
            throw new BadRequestException("OTP expired or not found");
        }

        if (!savedOtp.equals(otp)) {
            throw new BadRequestException("Invalid OTP");
        }

        PendingUserRegistration pendingUser = pendingRegistrationRedisService.getPendingUserByEmail(email);
        if (pendingUser == null) {
            throw new BadRequestException("Registration session expired. Please register again.");
        }

        AppUser existingEmail = appUserRepository.findByEmail(email);
        if (existingEmail != null) {
            throw new BadRequestException("Email already exists");
        }

        AppUser existingUsername = appUserRepository.findByUsername(pendingUser.getUsername());
        if (existingUsername != null) {
            throw new BadRequestException("Username already exists");
        }

        AppUser user = AppUser.builder()
                .name(pendingUser.getUsername())
                .email(pendingUser.getEmail())
                .password(pendingUser.getPassword())
                .level(1)
                .xp(0)
                .profileImageUrl(pendingUser.getProfileImageUrl())
                .isVerified(true)
                .build();

        appUserRepository.save(user);
        otpRedisService.deleteOtp(email);
        pendingRegistrationRedisService.deletePendingUser(pendingUser);
        rateLimitService.reset(rateKey);
    }

    @Override
    public void resend(String identifier) {
        PendingUserRegistration pendingUser =
                pendingRegistrationRedisService.getPendingUserByIdentifier(identifier);

        if (pendingUser == null) {
            throw new ResourceNotFoundException("Pending registration not found or expired");
        }

        String email = pendingUser.getEmail();

        if (otpRedisService.hasResendCooldown(email)) {
            throw new TooManyRequestsException("Please wait before requesting another OTP");
        }

        String otp = OtpUtil.generateSixDigitOtp();
        otpRedisService.saveOtp(email, otp, Duration.ofMinutes(otpTtlMinutes));
        otpRedisService.setResendCooldown(email, Duration.ofSeconds(resendCooldownSeconds));
        mailService.sendOtpVerificationEmail(email, pendingUser.getUsername(), otp);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String rateKey = "rate_limit:login:" + request.getIdentifier();
        boolean allowed = rateLimitService.isAllowed(rateKey, 5, Duration.ofMinutes(1));
        if (!allowed) {
            throw new TooManyRequestsException("Too many login attempts. Please try again later.");
        }

        AppUser user = appUserRepository.findByEmailOrUsername(request.getIdentifier());

        if (user != null) {
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new UnauthorizedException("Invalid email/username or password");
            }

            if (!Boolean.TRUE.equals(user.getIsVerified())) {
                throw new UnauthorizedException("Your account is not verified yet. Please verify your email before logging in.");
            }

            String token = jwtUtils.generateToken(user.getEmail());
            rateLimitService.reset(rateKey);

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .appUserId(user.getAppUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }

        PendingUserRegistration pendingUser =
                pendingRegistrationRedisService.getPendingUserByIdentifier(request.getIdentifier());

        if (pendingUser != null) {
            if (passwordEncoder.matches(request.getPassword(), pendingUser.getPassword())) {
                throw new UnauthorizedException("Your account is not verified yet. Please verify your email before logging in.");
            }
        }

        throw new UnauthorizedException("Invalid email/username or password");
    }
}