package com.dangeacademy.config.cloudflare.cloudflaredto.api;

import lombok.Data;

import java.util.List;

@Data
public class CloudflareApiResponse {

    private boolean success;

    private List<Object> errors;

    private List<Object> messages;

    private CloudflareResult result;

}