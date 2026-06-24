package com.dangeacademy.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dangeacademy.dto.VideoUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;


@Slf4j
@Service
public class AWSS3Service {

    @Autowired
    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    public VideoUploadResponse VideoUploadToAWSS3(MultipartFile video) {

        String contentType = video.getContentType();

        if (contentType == null || !contentType.startsWith("video/")) {
            throw new RuntimeException("Only video files are allowed");
        }
        
        String fileName = "Videos/" + System.currentTimeMillis() + "_" + video.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(video.getContentType());
        metadata.setContentLength(video.getSize());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    fileName,
                    video.getInputStream(),
                    metadata
            );
            client.putObject(putObjectRequest);
             /*String preSignedUrl = preSignedUrl(fileName);
            return  preSignedUrl;*/

             String videoUrl =  "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
             
             return new VideoUploadResponse(fileName,videoUrl);
             
             
        }catch (Exception e){
            throw new RuntimeException("Failed to upload video", e);
        }

    }


    public String ThumbnailUploadToAWSS3(MultipartFile thumbnail) {

        String contentType = thumbnail.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }

        String fileName = "Thumbnail/" + System.currentTimeMillis() + "_" + thumbnail.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(thumbnail.getContentType());
        metadata.setContentLength(thumbnail.getSize());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    fileName,
                    thumbnail.getInputStream(),
                    metadata
            );
            client.putObject(putObjectRequest);
             /*String preSignedUrl = preSignedUrl(fileName);
            return  preSignedUrl;*/

            /*String thumbnailUrl  =  "https://" + bucketName + ".s3.amazonaws.com/" + fileName;*/

            return fileName;


        }catch (Exception e){
            throw new RuntimeException("Failed to upload video", e);
        }

    }


    public  String preSignedUrl(String fileName){

        Date expiration = new Date();
        long expTimeMillis =  System.currentTimeMillis() + (60 * 60 * 1000); // 1 hour
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName,fileName)
                        .withMethod(HttpMethod.GET);
                        /*.withExpiration(expiration);*/

        URL url= client.generatePresignedUrl( generatePresignedUrlRequest);

        return  url.toString();
    }

    public String uploadFile(MultipartFile thumbnail) {
        return " ";
    }

   /* public String getImageURLByName(String fileName){
        S3Object object = client.getObject(bucketName, fileName);
        String key = object.getKey();
        String url = preSignedUrl(key);
        return  url;

    }*/


}

