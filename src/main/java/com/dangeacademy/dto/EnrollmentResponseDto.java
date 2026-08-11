package com.dangeacademy.dto;

import com.dangeacademy.entity.Course;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EnrollmentResponseDto {

    private Long id;

    private CourseResponseDto course;

    private LocalDateTime enrolledAt;

    private LocalDateTime expireOn;
    private double progressPercentage;
}