package com.dangeacademy.controller;

import com.dangeacademy.dto.VideoUploadResponse;
import com.dangeacademy.service.AWSS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/admin/videos")
public class S3Controller {

    @Autowired
    AWSS3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<VideoUploadResponse> uploadVideo(@RequestParam MultipartFile file){
        return ResponseEntity.ok(s3Service.VideoUploadToAWSS3(file));
    }

    @GetMapping()
    public ResponseEntity<String> getVideFromAWS(@RequestParam String fileName){
        return ResponseEntity.ok(s3Service.preSignedUrl(fileName));

    }
}
