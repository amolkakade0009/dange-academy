package com.dangeacademy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    public void sendOtp(String email, String otp) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.brevo.com/v3/smtp/email";

        // 1. Set the standard HTTP headers + Brevo API Key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        // 2. Format your specific OTP message
        String textContent = "Your OTP is Danges Academy: " + otp + "\n\nThis OTP is valid for 5 minutes.";

        // 3. Build the JSON body expected by Brevo
        Map<String, Object> body = Map.of(
                "sender", Map.of("name", senderName, "email", senderEmail),
                "to", List.of(Map.of("email", email)),
                "subject", "Password Reset OTP",
                "textContent", textContent
        );

        // 4. Package it together
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // 5. Send the HTTPS request
        try {
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("OTP Email sent successfully to " + email);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Email sending failed");
        }
    }
}