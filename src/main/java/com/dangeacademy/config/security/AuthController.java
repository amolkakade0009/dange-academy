package com.dangeacademy.config.security;


import com.dangeacademy.config.security.dto.LoginRequest;
import com.dangeacademy.config.security.dto.SignupRequest;
import com.dangeacademy.config.security.service.AuthService;
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
    public ResponseEntity logoutuser(@Valid @PathVariable Long userId)
    {
        return ResponseEntity.ok(authService.logout(userId));
    }

}