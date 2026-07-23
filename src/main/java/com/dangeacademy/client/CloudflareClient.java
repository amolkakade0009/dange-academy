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

@Component
@RequiredArgsConstructor
public class CloudflareClient {

    private final WebClient cloudflareWebClient;
    private final CloudflareConfig cloudflareConfig;


    /**
     * Creates a Direct Upload URL (TUS Upload)
     */
    public CloudflareUploadResponse createUpload() {

        try {

            CloudflareApiResponse response = cloudflareWebClient
                    .post()
                    .uri(cloudflareConfig.createUploadUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "maxDurationSeconds", 7200
                    ))
                    .retrieve()
                    .bodyToMono(CloudflareApiResponse.class)
                    .retry(2)
                    .block();

            if (response == null || response.getResult() == null) {
                throw new CloudflareException("Cloudflare returned an empty response.");
            }

            return new CloudflareUploadResponse(
                    response.getResult().getUid(),
                    response.getResult().getUploadURL()
            );

        } catch (WebClientResponseException e) {

            throw new CloudflareException(
                    "Cloudflare API Error: " + e.getResponseBodyAsString(),
                    e
            );

        } catch (Exception e) {

            throw new CloudflareException(
                    "Failed to create upload URL.",
                    e
            );
        }
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