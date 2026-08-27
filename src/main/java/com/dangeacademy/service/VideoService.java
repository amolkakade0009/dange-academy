package com.dangeacademy.service;

import com.dangeacademy.client.CloudflareClient;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareUploadResponse;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareVideoStatusResponse;
import com.dangeacademy.dto.VideoUploadReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final CloudflareClient cloudflareClient;


    public CloudflareUploadResponse createUpload(VideoUploadReq req) {

        return cloudflareClient.createTusUpload(
                req.getFileSize(),
                req.getFileName(),
                req.getContentType()
        );

    }

    public String generatePlaybackToken(String videoUid) {

        return cloudflareClient.generatePlaybackToken(videoUid);

    }


    public CloudflareVideoStatusResponse getVideoDetails(String videoUid) {

        return cloudflareClient.getVideoDetails(videoUid);

    }


    public void deleteVideo(String videoUid) {
        try{
            cloudflareClient.deleteVideo(videoUid);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}