package com.dangeacademy.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardAnalyticsResponse(
        List<MonthlyRevenue> revenueTrend,
        List<CourseEnrollment> enrollmentsByCourse
) {
    // Nested records are implicitly public and static
    public record MonthlyRevenue(String month, BigDecimal revenue) {}

    public record CourseEnrollment(String name, long students) {}
}