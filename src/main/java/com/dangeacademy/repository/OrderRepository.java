package com.dangeacademy.repository;

import com.dangeacademy.entity.Order;
import com.dangeacademy.enums.OrderStatus;
import com.dangeacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);

    List<Order> findByUser(User user);

    List<Order> findByStatus(OrderStatus status);
}