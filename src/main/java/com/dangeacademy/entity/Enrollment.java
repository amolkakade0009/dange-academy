package com.dangeacademy.entity;

import com.dangeacademy.dto.UserResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(unique = true)
    @NotNull(message = "razorpayOrderId is required")
    private String razorpayOrderId;

    @Column
    @NotNull(message = "razorpayPaymentId is required")
    private  String razorpayPaymentId;

    @Column
    private LocalDateTime enrolledAt;

    @Column
    private LocalDateTime expireOn;

}
