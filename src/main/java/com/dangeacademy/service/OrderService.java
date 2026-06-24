package com.dangeacademy.service;

import com.dangeacademy.entity.Order;
import com.dangeacademy.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userId, Long courseId);

    void verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature);

    Order getOrderById(Long orderId);

    List<Order> getOrdersByUser(Long userId);

    List<Order> getAllOrders();

    List<Order> getOrdersByStatus(OrderStatus status);

}