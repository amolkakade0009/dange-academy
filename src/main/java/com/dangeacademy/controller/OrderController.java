package com.dangeacademy.controller;

import com.dangeacademy.entity.Order;
import com.dangeacademy.entity.OrderStatus;
import com.dangeacademy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    /**
     * Create Razorpay Order
     */
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(
            @RequestParam Long userId,
            @RequestParam Long courseId) {

        Order order = orderService.createOrder(userId, courseId);
        return ResponseEntity.ok(order);
    }

    /**
     * Verify payment and update order status
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody Map<String, String> paymentData) {

        orderService.verifyPayment(
                paymentData.get("razorpayOrderId"),
                paymentData.get("razorpayPaymentId"),
                paymentData.get("razorpaySignature")
        );

        return ResponseEntity.ok("Payment verified successfully");
    }

    /**
     * Get order by id
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }

    /**
     * Get all orders of a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId)
        );
    }

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @PathVariable OrderStatus status) {

        return ResponseEntity.ok(
                orderService.getOrdersByStatus(status)
        );
    }


}