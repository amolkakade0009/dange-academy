package com.dangeacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void sendOtp(String email, String otp) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Password Reset OTP");

        message.setText(
                "Your OTP is Danges Academy: " + otp +
                        "\n\nThis OTP is valid for 5 minutes."
        );

        mailSender.send(message);
    }
}