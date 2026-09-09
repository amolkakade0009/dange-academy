package com.dangeacademy.service;

import com.dangeacademy.dto.DashboardSummaryDTO;
import com.dangeacademy.entity.Order;
import com.dangeacademy.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    Order createOrder(Long userId, Long courseId);

    void verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature);

    Order getOrderById(Long orderId);

    List<Order> getOrdersByUser(Long userId);

    List<Order> getAllOrders(LocalDate startDate, LocalDate endDate);

    List<Order> getOrdersByStatus(OrderStatus status);

    DashboardSummaryDTO getDashboardSummary(LocalDate startDate, LocalDate endDate);
}