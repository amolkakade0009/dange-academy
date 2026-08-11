package com.dangeacademy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Rohan Ghuge
 * @since 09-08-2026
 */

@Data
@AllArgsConstructor
public class CourseProgressResponse {
    private Long courseId;
    private double progressPercentage;
    private int completedSubTopics;
    private int totalSubTopics;
}