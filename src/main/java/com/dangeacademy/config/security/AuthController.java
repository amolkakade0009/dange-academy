package com.dangeacademy.config.security;


import com.dangeacademy.config.security.dto.LoginRequest;
import com.dangeacademy.config.security.dto.SignupRequest;
import com.dangeacademy.config.security.service.AuthService;
import com.dangeacademy.dto.ResetPasswordRequest;
import com.dangeacademy.dto.VerifyOtpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public String signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/student/logout/{userId}")
    public ResponseEntity logoutuser(@Valid @PathVariable Long userId) {
        return ResponseEntity.ok(authService.logout(userId));
    }


    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {

        authService.sendPasswordResetOtp(request.get("email"));

        return ResponseEntity.ok(
                "OTP sent successfully"
        );
    }

    @PostMapping("/auth/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {

        String resetToken =
                authService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP verified successfully",

                        "resetToken",
                        resetToken
                )
        );
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        authService.resetPassword(
                request.getResetToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                "Password changed successfully"
        );
    }
}