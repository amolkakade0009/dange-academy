package com.dangeacademy.service;

import com.dangeacademy.dto.DashboardAnalyticsResponse;
import com.dangeacademy.dto.DashboardAnalyticsResponse.CourseEnrollment;
import com.dangeacademy.dto.DashboardAnalyticsResponse.MonthlyRevenue;
import com.dangeacademy.entity.Order;
import com.dangeacademy.enums.OrderStatus;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final CourseRepository courseRepository; // Injected

    public DashboardAnalyticsResponse getCurrentYearAnalytics() {
        int currentYear = LocalDate.now().getYear();

        LocalDateTime startOfYear = LocalDateTime.of(LocalDate.of(currentYear, 1, 1), LocalTime.MIN);
        LocalDateTime endOfYear = LocalDateTime.of(LocalDate.of(currentYear, 12, 31), LocalTime.MAX);

        // 1. Revenue trend from OrderRepository
        List<Order> paidOrders = orderRepository.findPaidOrdersWithCourse(
                OrderStatus.PAID,
                startOfYear,
                endOfYear
        );

        Map<Month, BigDecimal> revenueByMonth = paidOrders.stream()
                .filter(o -> o.getPaidAt() != null && o.getAmount() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getPaidAt().getMonth(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                o -> BigDecimal.valueOf(o.getAmount()),
                                BigDecimal::add
                        )
                ));

        List<MonthlyRevenue> revenueTrend = Arrays.stream(Month.values())
                .filter(revenueByMonth::containsKey)
                .map(month -> new MonthlyRevenue(
                        month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        revenueByMonth.getOrDefault(month, BigDecimal.ZERO)
                ))
                .toList();

        // 2. Fetch Enrollments directly from Course.enrolledCount
        List<CourseEnrollment> enrollmentsByCourse = courseRepository
                .findByEnrolledCountGreaterThanOrderByEnrolledCountDesc(0)
                .stream()
                .map(course -> new CourseEnrollment(
                        course.getCourseName(),
                        course.getEnrolledCount() != null ? course.getEnrolledCount().longValue() : 0L
                ))
                .toList();

        return new DashboardAnalyticsResponse(revenueTrend, enrollmentsByCourse);
    }
}