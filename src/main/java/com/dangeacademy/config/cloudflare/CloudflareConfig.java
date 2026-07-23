package com.dangeacademy.config.cloudflare;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CloudflareConfig {

    private final CloudflareProperties properties;

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

}