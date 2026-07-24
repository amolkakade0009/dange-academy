package com.dangeacademy.dto;

import com.dangeacademy.entity.Course;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EnrollmentResponseDto {

    private Long id;

    private UserResponseDto student;

    private Course course;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private LocalDateTime enrolledAt;

    private LocalDateTime expireOn;
}