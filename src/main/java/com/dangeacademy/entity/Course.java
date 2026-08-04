package com.dangeacademy.entity;

import com.dangeacademy.enums.CourseCategory;
import com.dangeacademy.enums.CourseStatus;
import com.dangeacademy.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Size(max = 1000)
    @Column(nullable = false)
    private String description;

    @NotNull(message = "price is required")
    @Positive(message = "price must be greater than 0")
    @Column(nullable = false)
    private Double price;  //Discounted price

    private Double originalPrice;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column
    private String courseThumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseCategory category;

    @Column
    private String introVideoUid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoStatus introVideoStatus = VideoStatus.PROCESSING;

    @Column
    private Long courseValidity; // in months

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedDate;

    @Column
    private LocalDateTime updatedDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer enrolledCount = 0;

    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<Chapter> chapters = new ArrayList<>();



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
