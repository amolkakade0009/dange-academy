package com.dangeacademy.config.cloudflare;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CloudflareConfig {

    private final CloudflareProperties properties;

    // ADD THIS NEW METHOD FOR TUS
    public String createTusUploadUrl() {
        // The ?direct_user=true parameter is required to allow the
        // frontend/browser to perform the upload directly.
        return "/accounts/" + properties.getAccountId() + "/stream?direct_user=true";
    }

    // --- DO NOT CHANGE EXISTING METHODS BELOW ---

    public String createUploadUrl() {
        return "/accounts/" + properties.getAccountId() + "/stream/direct_upload";
    }

    public String videoDetailsUrl(String videoUid) {
        return "/accounts/" + properties.getAccountId() + "/stream/" + videoUid;
    }

    public String deleteVideoUrl(String videoUid) {
        return "/accounts/" + properties.getAccountId() + "/stream/" + videoUid;
    }

    public String playbackTokenUrl(String videoUid) {
        return "/accounts/" + properties.getAccountId()
                + "/stream/" + videoUid + "/token";
    }

    // Added helper to get Account ID if needed in the Service
    public String getAccountId() {
        return properties.getAccountId();
    }
}