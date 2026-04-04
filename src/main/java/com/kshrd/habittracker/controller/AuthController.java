package com.kshrd.habittracker.controller;

import com.kshrd.habittracker.dto.request.auth.LoginRequest;
import com.kshrd.habittracker.dto.request.auth.RegisterRequest;
import com.kshrd.habittracker.dto.response.ApiResponse;
import com.kshrd.habittracker.dto.response.AuthResponse;
import com.kshrd.habittracker.dto.response.appuser.AppUserResponse;
import com.kshrd.habittracker.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AppUserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(authService.register(request), "User Registered successfully. Please verify your email to complete the registration.", HttpStatus.CREATED)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verify(
            @Valid
            @Email @RequestParam String email,
            @Size(min = 6, max = 6) @RequestParam String otp
    ) {
        authService.verify(email, otp);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Email successfully verified. You can now log in.", HttpStatus.OK)
        );
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<Void>> resend(
            @Valid
            @Email
            @RequestParam String email) {
        authService.resend(email);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Verification OTP resent successfully", HttpStatus.OK)

        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(authService.login(request), "Login successful. Authentication token generated", HttpStatus.OK)

        );
    }
}