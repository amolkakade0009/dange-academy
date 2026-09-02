package com.dangeacademy.utility;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepAliveTask {
    private final RestTemplate restTemplate = new RestTemplate();

    // 600,000 milliseconds = 10 minutes
    @Scheduled(fixedRate = 600000)
    public void pingMyself() {
        try {
            // Replace with your actual live Render deployment URL
            String url = "https://dange-academy.onrender.com/public/health";
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Keep-alive ping sent successfully: " + response);
        } catch (Exception e) {
            System.err.println("Keep-alive ping failed: " + e.getMessage());
        }
    }
}