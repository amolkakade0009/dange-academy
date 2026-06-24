package com.dangeacademy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "sub_topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topicName;

    @Column(length = 2000)
    private String content;

    private Integer topicOrder;

    private String videoUrl;

    private Long durationInSeconds;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}