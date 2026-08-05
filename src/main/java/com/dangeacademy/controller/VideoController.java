package com.dangeacademy.controller;

import com.dangeacademy.config.cloudflare.CloudflareProperties;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareUploadResponse;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareVideoStatusResponse;
import com.dangeacademy.config.cloudflare.cloudflaredto.api.PlaybackUrlResponse;
import com.dangeacademy.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    private  final CloudflareProperties properties;

    /**
     * Create Direct Upload URL
     */
    @PostMapping("/admin/videos/upload-url")
    public ResponseEntity<CloudflareUploadResponse> createUpload() {

        CloudflareUploadResponse response = videoService.createUpload();

        return ResponseEntity.ok(response);
    }


    /**
     * Generate Playback Token
     */
    @GetMapping("student/course/{videoUid}/playback-url")
    public ResponseEntity<PlaybackUrlResponse> getPlaybackUrl(
            @PathVariable String videoUid) {

        String token = videoService.generatePlaybackToken(videoUid);

        String url = "https://"
                + properties.getCustomerSubdomain()
                + "/"
                + videoUid
                + "/iframe?token="
                + token;

        return ResponseEntity.ok(new PlaybackUrlResponse(url));
    }

    @GetMapping("public/course/{videoUid}/playback-url")
    public ResponseEntity<PlaybackUrlResponse> getPlaybackUrlForPublic(
            @PathVariable String videoUid) {

        String token = videoService.generatePlaybackToken(videoUid);

        String url = "https://"
                + properties.getCustomerSubdomain()
                + "/"
                + videoUid
                + "/iframe?token="
                + token;

        return ResponseEntity.ok(new PlaybackUrlResponse(url));
    }


    /**
     * Get Video Details
     */
    @GetMapping("/{videoUid}")
    public ResponseEntity<CloudflareVideoStatusResponse> getVideoDetails(
            @PathVariable String videoUid) {

        CloudflareVideoStatusResponse response =
                videoService.getVideoDetails(videoUid);

        return ResponseEntity.ok(response);
    }


    /**
     * Delete Video
     */
    @DeleteMapping("/{videoUid}")
    public ResponseEntity<Void> deleteVideo(
            @PathVariable String videoUid) {

        videoService.deleteVideo(videoUid);

        return ResponseEntity.noContent().build();
    }

}