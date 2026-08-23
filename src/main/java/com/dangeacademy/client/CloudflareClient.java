package com.dangeacademy.client;

import com.dangeacademy.config.cloudflare.CloudflareConfig;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareUploadResponse;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareVideoStatusResponse;
import com.dangeacademy.config.cloudflare.cloudflaredto.PlaybackTokenApiResponse;
import com.dangeacademy.config.cloudflare.cloudflaredto.api.CloudflareApiResponse;
import com.dangeacademy.exception.CloudflareException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Helper to Base64 encode metadata values as per TUS spec
 */

@Component
@RequiredArgsConstructor
public class CloudflareClient {

    private final WebClient cloudflareWebClient;
    private final CloudflareConfig cloudflareConfig;


    /**
     * Creates a Direct Upload URL (TUS Upload)
     */
    public CloudflareUploadResponse createTusUpload(long fileSize, String fileName, String contentType) {
        try {
            // 1. Prepare TUS Metadata (Required by Cloudflare for TUS)
            // Values must be Base64 encoded and comma-separated
            String metadata = String.format("filename %s,filetype %s",
                    toBase64(fileName),
                    toBase64(contentType));

            return cloudflareWebClient
                    .post()
                    // URL: /accounts/{account_id}/stream?direct_user=true
                    .uri(uriBuilder -> uriBuilder
                            .path("/accounts/{accountId}/stream")
                            .queryParam("direct_user", "true")
                            .build(cloudflareConfig.getAccountId()))
                    .header("Tus-Resumable", "1.0.0")
                    .header("Upload-Length", String.valueOf(fileSize))
                    .header("Upload-Metadata", metadata)
                    .contentType(MediaType.APPLICATION_JSON)
                    // We use exchangeToMono because the data we need is in the HEADERS, not the body
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            HttpHeaders headers = response.headers().asHttpHeaders();
                            String uploadUrl = headers.getFirst(HttpHeaders.LOCATION);
                            String videoUid = headers.getFirst("stream-media-id");

                            if (uploadUrl == null || videoUid == null) {
                                return Mono.error(new CloudflareException("Cloudflare did not return TUS headers."));
                            }

                            return Mono.just(new CloudflareUploadResponse(videoUid, uploadUrl));
                        } else {
                            return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new CloudflareException("Cloudflare API Error: " + errorBody)));
                        }
                    })
                    .retry(2)
                    .block();

        } catch (WebClientResponseException e) {
            throw new CloudflareException("Cloudflare API Error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new CloudflareException("Failed to create TUS upload session: " + e.getMessage(), e);
        }
    }

    private String toBase64(String value) {
        if (value == null) return "";
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }




    public String generatePlaybackToken(String videoUid) {

        try {

            PlaybackTokenApiResponse response = cloudflareWebClient
                    .post()
                    .uri(cloudflareConfig.playbackTokenUrl(videoUid))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{}")
                    .retrieve()
                    .bodyToMono(PlaybackTokenApiResponse.class)
                    .block();

            if (response == null
                    || !response.isSuccess()
                    || response.getResult() == null
                    || response.getResult().getToken() == null
                    || response.getResult().getToken().isBlank()) {

                throw new CloudflareException("Cloudflare returned an empty playback token.");
            }

            return response.getResult().getToken();

        } catch (WebClientResponseException e) {

            throw new CloudflareException(
                    "Cloudflare API Error: " + e.getResponseBodyAsString(),
                    e
            );

        } catch (Exception e) {

            throw new CloudflareException(
                    "Failed to generate playback token.",
                    e
            );
        }
    }



    public void deleteVideo(String videoUid) {

        try {

            cloudflareWebClient
                    .delete()
                    .uri(cloudflareConfig.deleteVideoUrl(videoUid))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

        } catch (WebClientResponseException e) {

            throw new CloudflareException(
                    "Cloudflare API Error: " + e.getResponseBodyAsString(),
                    e
            );

        } catch (Exception e) {

            throw new CloudflareException(
                    "Failed to delete video.",
                    e
            );
        }
    }



    public CloudflareVideoStatusResponse getVideoDetails(String videoUid) {

        try {

            CloudflareApiResponse response = cloudflareWebClient
                    .get()
                    .uri(cloudflareConfig.videoDetailsUrl(videoUid))
                    .retrieve()
                    .bodyToMono(CloudflareApiResponse.class)
                    .block();

            if (response == null || response.getResult() == null) {
                throw new CloudflareException("Video not found.");
            }

            return new CloudflareVideoStatusResponse(
                    response.getResult().getUid(),
                    response.getResult().getStatus().getState(),
                    response.getResult().isReadyToStream(),
                    response.getResult().getDuration()
            );

        } catch (WebClientResponseException e) {

            throw new CloudflareException(
                    "Cloudflare API Error: " + e.getResponseBodyAsString(),
                    e
            );

        } catch (Exception e) {
            System.out.println("Error : "+e);

            throw new CloudflareException(
                    "Failed to fetch video details.",
                    e
            );
        }
    }

}