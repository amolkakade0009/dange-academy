package com.dangeacademy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private String videoUid;

    private double durationInSeconds;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonBackReference
    private Chapter chapter;
}