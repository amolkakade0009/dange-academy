package com.dangeacademy.service.impl;

import com.dangeacademy.entity.Course;
import com.dangeacademy.entity.Order;
import com.dangeacademy.enums.OrderStatus;
import com.dangeacademy.entity.User;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.repository.OrderRepository;
import com.dangeacademy.repository.UserRepository;
import com.dangeacademy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.razorpay.RazorpayClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    private RazorpayClient razorpayClient;

    @Override
    public Order createOrder(Long userId, Long courseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with id : " + courseId));

        try {

            // Create Razorpay Order
            JSONObject orderRequest = new JSONObject();

            orderRequest.put("amount", (int)(course.getPrice() * 100)); // paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "course_" + courseId + "_" + System.currentTimeMillis());

            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);


            // Save Order in DB
            Order order = new Order();

            order.setRazorpayOrderId(razorpayOrder.get("id"));
            order.setAmount(course.getPrice().doubleValue());
            order.setCurrency("INR");
            order.setStatus(OrderStatus.CREATED);
            order.setCreatedAt(LocalDateTime.now());

            order.setUser(user);
            order.setCourse(course);

            return orderRepository.save(order);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    @Override
    public void verifyPayment(String razorpayOrderId,
                              String razorpayPaymentId,
                              String razorpaySignature) {

        Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with Razorpay Order Id : "
                                        + razorpayOrderId));

        order.setRazorpayPaymentId(razorpayPaymentId);
        order.setRazorpaySignature(razorpaySignature);
        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id : " + orderId));
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + userId));

        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {

        return orderRepository.findByStatus(status);
    }


}