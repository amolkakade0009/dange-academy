package com.dangeacademy.service;

import com.dangeacademy.client.CloudflareClient;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareUploadResponse;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareVideoStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final CloudflareClient cloudflareClient;


    public CloudflareUploadResponse createUpload() {

        return cloudflareClient.createUpload();

    }


    public String generatePlaybackToken(String videoUid) {

        return cloudflareClient.generatePlaybackToken(videoUid);

    }


    public CloudflareVideoStatusResponse getVideoDetails(String videoUid) {

        return cloudflareClient.getVideoDetails(videoUid);

    }


    public void deleteVideo(String videoUid) {

        cloudflareClient.deleteVideo(videoUid);

    }

}