package com.dangeacademy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoUploadResponse {

    private String videoKey;
    private String videoUrl;
}