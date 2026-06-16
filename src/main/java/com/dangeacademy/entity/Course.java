package com.dangeacademy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course Name is required")
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true)
    private String courseName;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000)
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Fees is required")
    @Positive(message = "Fees must be greater than 0")
    @Column(nullable = false)
    private Double fees;

    @NotBlank(message = "Video URL is required")
    @Column(nullable = false)
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedDate;

    private LocalDateTime updatedDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer enrolledCount = 0;

    @PrePersist
    public void prePersist() {
        this.uploadedDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

}
