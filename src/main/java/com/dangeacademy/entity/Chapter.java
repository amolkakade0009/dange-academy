package com.dangeacademy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "chapters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chapterName;

    @Column(length = 1000)
    private String description;

    private Integer chapterOrder;

    private Long durationInSeconds;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SubTopic> subTopics = new ArrayList<>();
}