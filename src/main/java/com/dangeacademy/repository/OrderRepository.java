package com.dangeacademy.repository;

import com.dangeacademy.entity.Order;
import com.dangeacademy.enums.OrderStatus;
import com.dangeacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);

    List<Order> findByUser(User user);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Finds orders between two dates and sorts them by paidAt descending (newest first)
    List<Order> findByPaidAtBetweenOrderByPaidAtDesc(LocalDateTime startDate, LocalDateTime endDate);



}