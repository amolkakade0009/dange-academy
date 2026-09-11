package com.dangeacademy.dto;


import lombok.Data;

@Data
public class DashboardSummaryDTO {
    private Double totalRevenue;
    private Long totalStudents;
    private Long totalCourses;
    private int totalTransactions;

    public DashboardSummaryDTO(Double totalRevenue, Long totalStudents, Long totalCourses , int totalTransactions) {
        this.totalRevenue = totalRevenue;
        this.totalStudents = totalStudents;
        this.totalCourses = totalCourses;
        this .totalTransactions = totalTransactions;
    }


}
