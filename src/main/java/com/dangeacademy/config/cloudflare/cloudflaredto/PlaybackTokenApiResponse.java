package com.dangeacademy.config.cloudflare.cloudflaredto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaybackTokenApiResponse {

    private boolean success;
    private Result result;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {

        private String token;

    }
}