package com.dangeacademy.config.security.service;



import com.dangeacademy.config.security.dto.LoginRequest;
import com.dangeacademy.config.security.dto.SignupRequest;
import com.dangeacademy.entity.PasswordReset;
import com.dangeacademy.enums.Role;
import com.dangeacademy.entity.User;
import com.dangeacademy.repository.PasswordResetRepository;
import com.dangeacademy.repository.UserRepository;
import com.dangeacademy.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailService emailService;

    public String signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        //Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobileNumber(request.getMobileNumber());
        user.setRole(Role.STUDENT);


        userRepository.save(user);

        return "User Registered Successfully";
    }


    public Map<String, String> login(LoginRequest request) {

        // 1. Authenticate Email and Password first
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        boolean isAdmin = String.valueOf(user.getRole()).equals("ADMIN");
        boolean otpMissing = request.getOtp() == null || request.getOtp().isBlank();

        // 2. ADMIN FLOW: If Admin and no OTP provided, send OTP and stop.
        if (isAdmin && otpMissing) {
            return sendLoginOtpForAdmin(user);
        }

        // 3. ADMIN FLOW: If Admin and OTP *is* provided, verify it!
        if (isAdmin && !otpMissing) {
            Boolean isOtpValid = verifyOtpOFAdmin(user.getEmail(), request.getOtp());

            // SECURITY FIX: Actually check the result! Stop them if the OTP is wrong.
            if (!isOtpValid) {
                throw new RuntimeException("Invalid or expired OTP");
                // Or throw a custom exception like BadCredentialsException
            }
        }

        // Generate NEW session ID
        String sessionId = UUID.randomUUID().toString();

        // Replace previous session
        user.setSessionId(sessionId);

        userRepository.save(user);

        String token = jwtService.generateToken(user, sessionId);
        System.out.println("Session Id"+ "=" + sessionId );
        System.out.println("Token"+ "=" + token );


        Map<String,String> login_response=new HashMap<>();
        login_response.put("token",token);
        login_response.put("user_id",user.getId().toString());
        login_response.put("role",user.getRole().toString());
        login_response.put("email", user.getEmail());
        login_response.put("name",user.getName());
        login_response.put("mobileNumber",user.getMobileNumber());
        login_response.put("sessionId", sessionId);

        return login_response;
    }

    Map<String, String> sendLoginOtpForAdmin(User user) {

        String otp = generateOtp();

        PasswordReset passwordReset =
                passwordResetRepository
                        .findByEmail(user.getEmail())
                        .orElse(new PasswordReset());

        passwordReset.setEmail(user.getEmail());
        passwordReset.setOtp(otp);
        passwordReset.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );

        passwordReset.setResetToken(null);
        passwordReset.setResetTokenExpiresAt(null);

        passwordResetRepository.save(passwordReset);
        System.out.println(
                "OTP SAVED FOR EMAIL = " + user.getEmail()
        );
        System.out.println(
                "OTP = " + otp
        );

        emailService.sendOtp(user.getEmail(), otp);

        return Map.of(
                "Admin Otp Response",
                "OTP sent successfully to email"
        );
    }



    public Map<String, String> logout(Long userId)
    {
        User  user=userRepository.findById(userId).orElseThrow();
        /*user.setIsLogin(false);*/
        user.setSessionId("");
        userRepository.save(user);
        Map<String,String> logout_response=new HashMap<>();
        logout_response.put("msg","Logout Successfull");
        return logout_response;
    }


    public void sendPasswordResetOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        String otp = generateOtp();

        PasswordReset passwordReset =
                passwordResetRepository
                        .findByEmail(email)
                        .orElse(new PasswordReset());

        passwordReset.setEmail(email);

        passwordReset.setOtp(otp);

        passwordReset.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );

        // Remove old reset token
        passwordReset.setResetToken(null);
        passwordReset.setResetTokenExpiresAt(null);

        passwordResetRepository.save(passwordReset);

        emailService.sendOtp(email, otp);
    }


    public String verifyOtp(String email, String otp) {

        PasswordReset passwordReset =
                passwordResetRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "OTP not found"
                                )
                        );

        // Check OTP expiry
        if (passwordReset.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }

        // Check OTP
        if (!passwordReset.getOtp().equals(otp)) {

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }

        // Generate reset token
        String resetToken = generateResetToken();

        passwordReset.setResetToken(resetToken);

        passwordReset.setResetTokenExpiresAt(
                LocalDateTime.now().plusMinutes(10)
        );

        // OTP can no longer be used
        passwordReset.setOtp(null);
        passwordReset.setExpiresAt(null);

        passwordResetRepository.save(passwordReset);

        return resetToken;
    }

    public void resetPassword(String resetToken, String newPassword) {

        PasswordReset passwordReset = passwordResetRepository
                .findByResetToken(resetToken)
                .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid reset token"
                                )
                        );

        // Check token expiry
        if (passwordReset.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(
                    "Reset token expired"
            );
        }

        // Get email from reset record
        String email = passwordReset.getEmail();

        // Find user
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        // Hash password
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Invalidate reset token
        passwordResetRepository.delete(passwordReset);
    }


    public Boolean verifyOtpOFAdmin(String email, String otp) {

        PasswordReset passwordReset =
                passwordResetRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "OTP not found"
                                )
                        );

        // Check OTP expiry
        if (passwordReset.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }

        // Check OTP
        if (!passwordReset.getOtp().equals(otp)) {

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }

        passwordResetRepository.delete(passwordReset);

        return true;
    }

    private String generateOtp() {

        return String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 1000000)
        );
    }

    private String generateResetToken() {

        return UUID.randomUUID().toString();
    }
}