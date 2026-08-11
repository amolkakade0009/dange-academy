package com.dangeacademy.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
/**
 * @author Rohan Ghuge
 * @since 09-08-2026
 */


@Entity
@Table(
        name = "video_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sub_topic_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_topic_id", nullable = false)
    private SubTopic subTopic;

    @Column(nullable = false)
    private double lastPositionInSeconds; // Where student stopped

    @Column(nullable = false)
    private double maxWatchedInSeconds; // Furthest point reached

    @Column(nullable = false)
    private boolean isCompleted; // True if watched >= 90%

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void onSave() {
        this.updatedAt = LocalDateTime.now();
    }
}