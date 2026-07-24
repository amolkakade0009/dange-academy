package com.dangeacademy.rzp;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
/**
 * @author Rohan Ghuge
 * @since 21-07-2026
 */
@RestController
@RequestMapping("student/order")
public class OrderController {

    private final  OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Request DTOs
    public record CreateOrderReq(double price, String courseName) {}
    public record VerifyReq(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature ,Long stundetid, Long courseId , LocalDateTime enrolledAt) {}

    @GetMapping("/create-order/{courseId}/{StudentId}")
    public ResponseEntity<?> createOrder(@PathVariable Long courseId, @PathVariable Long studentId) {

        try {

            Map<String, Object> orderData = orderService.createOrder(courseId,studentId);
            return ResponseEntity.ok(orderData);
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyReq req) {
        boolean isSuccess = orderService.verifyPayment(
                req.razorpayOrderId(),
                req.razorpayPaymentId(),
                req.razorpaySignature(),
                req.stundetid(),
                req.courseId(),
                req.enrolledAt()
        );

        if (isSuccess) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Payment verified successfully!"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "FAILED", "message", "Invalid signature!"));
        }
    }
}