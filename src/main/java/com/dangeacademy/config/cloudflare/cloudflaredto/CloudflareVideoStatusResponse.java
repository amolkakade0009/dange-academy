package com.dangeacademy.config.cloudflare.cloudflaredto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class CloudflareVideoStatusResponse {

    private String videoUid;

    private String status;

    private boolean ready;

    private double duration;

}