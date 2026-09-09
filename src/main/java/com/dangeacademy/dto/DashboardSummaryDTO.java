package com.dangeacademy.dto;


import lombok.Data;

@Data
public class DashboardSummaryDTO {
    private Double totalRevenue;
    private Long totalStudents;
    private Long totalCourses;

    public DashboardSummaryDTO(Double totalRevenue, Long totalStudents, Long totalCourses) {
        this.totalRevenue = totalRevenue;
        this.totalStudents = totalStudents;
        this.totalCourses = totalCourses;
    }


}
