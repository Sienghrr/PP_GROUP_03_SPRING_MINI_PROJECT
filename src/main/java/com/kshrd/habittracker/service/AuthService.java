package com.kshrd.habittracker.service;

import com.kshrd.habittracker.dto.request.auth.LoginRequest;
import com.kshrd.habittracker.dto.request.auth.RegisterRequest;
import com.kshrd.habittracker.dto.response.AuthResponse;
import com.kshrd.habittracker.dto.response.appuser.AppUserResponse;

public interface AuthService {
    AppUserResponse register(RegisterRequest request);
    void verify(String email, String otp);
    void resend(String email);
    AuthResponse login(LoginRequest request);
}
