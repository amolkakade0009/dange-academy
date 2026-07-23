package com.dangeacademy.config.cloudflare.cloudflaredto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudflareUploadResponse {

    private String videoUid;

    private String uploadUrl;

}