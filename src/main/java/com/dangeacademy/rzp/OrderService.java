package com.dangeacademy.rzp;


import com.dangeacademy.dto.UserResponseDto;
import com.dangeacademy.entity.Course;
import com.dangeacademy.entity.Enrollment;
import com.dangeacademy.entity.User;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.service.CourseService;
import com.dangeacademy.service.EnrollmentService;
import com.dangeacademy.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

   private final CourseService courseService;
   private final CourseRepository courseRepository;
   private final EnrollmentService enrollmentService;
   private final UserService userService;
    /**
     * Creates a static Razorpay Order without any DB interactions
     */
    public Map<String, Object> createOrder(Long courseId, Long studentId) throws RazorpayException {
        if(enrollmentService.isStudentEnrolled(studentId,courseId)){
           throw new RuntimeException("You have all ready enrolled");
        }

        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);


        Course course = courseService.getCourseById(courseId);


        long amountInPaise = Math.round(course.getPrice() * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "test_receipt_" + UUID.randomUUID().toString().substring(0, 8));

        // Call Razorpay API to generate order_id
        Order rzpOrder = razorpay.orders.create(orderRequest);
        String razorpayOrderId = rzpOrder.get("id");

        // Return payload for React
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", razorpayOrderId);
        response.put("amount", amountInPaise);
        response.put("currency", "INR");
        response.put("keyId", keyId);
        response.put("courseName", course.getCourseName());

        return response;
    }

    /**
     * Verifies payment signature without DB update
     */
    public boolean verifyPayment(String orderId, String paymentId,
                                 String signature, Long studentId ,
                                 Long courseId , LocalDateTime enrolledAt) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            // Verifies the HMAC-SHA256 signature
            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

            if(isValid){
                Course course = courseService.getCourseById(courseId);
                User user = userService.getUserById(studentId);

                Enrollment enrollment = new Enrollment();
                enrollment.setCourse(course);
                enrollment.setStudent(user);
                enrollment.setRazorpayOrderId(orderId);
                enrollment.setRazorpayPaymentId(paymentId);
                enrollment.setEnrolledAt(enrolledAt);
                enrollment.setExpireOn(
                        enrolledAt.plusMonths(course.getCourseValidity())
                );
                course.setEnrolledCount(course.getEnrolledCount()+1);

                enrollmentService.enrollStudent(enrollment);
                courseRepository.save(course);
            }


            return isValid;
        } catch (Exception e) {
            System.err.println("Verification Error: " + e.getMessage());
            return false;
        }
    }
}