package com.dangeacademy.dto;

import com.dangeacademy.entity.Mentor;
import com.dangeacademy.enums.CourseCategory;
import com.dangeacademy.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Rohan Ghuge
 * @since 25-07-2026
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

    private Long id;
    private String courseName;
    private String description;
    private Double price;
    private Double originalPrice;
    private String courseThumbnailUrl;
    private CourseCategory category;
    private Mentor mentor;
    private Long courseValidity;
    private CourseStatus status;
    private LocalDateTime uploadedDate;
    private Integer enrolledCount;
}

