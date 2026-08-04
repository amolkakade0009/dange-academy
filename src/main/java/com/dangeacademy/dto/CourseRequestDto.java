package com.dangeacademy.dto;

import com.dangeacademy.enums.CourseCategory;
import com.dangeacademy.enums.CourseStatus;
import com.dangeacademy.enums.VideoStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequestDto {

    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 100, message = "Course name must be between 3 and 100 characters")
    private String courseName;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @Positive(message = "Original price must be greater than 0")
    private Double originalPrice;

    @NotNull(message = "Mentor id is required")
    private Long mentorId;

    @NotNull(message = "Thumbnail  is required")
    private String courseThumbnailUrl;

    @NotNull(message = "Category is required")
    private CourseCategory category;

    @NotNull(message = "IntroVideoUid is required")
    private String introVideoUid;

    private VideoStatus introVideoStatus = VideoStatus.PROCESSING;



    @NotNull(message = "Course validity is required")
    @Positive(message = "Course validity must be greater than 0")
    private Long courseValidity;

    @NotNull(message = "Course status is required")
    private CourseStatus status;
}