package com.dangeacademy.config.cloudflare;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloudflare")
public class CloudflareProperties {

    private String accountId;

    private String apiToken;

    private String apiBaseUrl;

    private String customerSubdomain;

}