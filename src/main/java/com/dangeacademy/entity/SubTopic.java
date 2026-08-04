package com.dangeacademy.entity;

import com.dangeacademy.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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

    private String videoUid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoStatus videoStatus = VideoStatus.PROCESSING;

    private double durationInSeconds;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonBackReference
    private Chapter chapter;
}