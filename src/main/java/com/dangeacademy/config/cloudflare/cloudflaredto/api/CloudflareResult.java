package com.dangeacademy.config.cloudflare.cloudflaredto.api;

import lombok.Data;

@Data
public class CloudflareResult {

    // Returned by Direct Upload API
    private String uid;
    private String uploadURL;

    // Returned by Video Details API
    private CloudflareStatus status;
    private boolean readyToStream;
    private Double duration;

}