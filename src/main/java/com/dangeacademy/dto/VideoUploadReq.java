package com.dangeacademy.dto;

import lombok.Data;

/**
 * @author Rohan Ghuge
 * @since 23-08-2026
 */
// VideoUploadRequest.java
@Data
public class VideoUploadReq {
    long fileSize;
    String fileName;
    String contentType;
}
