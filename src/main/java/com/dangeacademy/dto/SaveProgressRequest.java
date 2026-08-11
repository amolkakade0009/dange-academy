package com.dangeacademy.dto;

import lombok.Data;

/**
 * @author Rohan Ghuge
 * @since 09-08-2026
 */
@Data
public class SaveProgressRequest {
    private Long subTopicId;
    private double currentPositionSeconds;
}

