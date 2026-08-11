package com.dangeacademy.service;
import com.dangeacademy.dto.SaveProgressRequest;
import com.dangeacademy.entity.*;
import com.dangeacademy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rohan Ghuge
 * @since 09-08-2026
 */

@Service
@RequiredArgsConstructor
public class CourseProgressService {

    private final VideoProgressRepository videoProgressRepository;
    private final SubTopicRepository subTopicRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public void saveVideoProgress(User user, SaveProgressRequest request) {
        SubTopic subTopic = subTopicRepository.findById(request.getSubTopicId())
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));

        VideoProgress progress = videoProgressRepository
                .findByUserIdAndSubTopicId(user.getId(), subTopic.getId())
                .orElseGet(() -> VideoProgress.builder()
                        .user(user)
                        .subTopic(subTopic)
                        .maxWatchedInSeconds(0)
                        .isCompleted(false)
                        .build());

        // Update last watched position (for resuming)
        progress.setLastPositionInSeconds(request.getCurrentPositionSeconds());
        System.out.println(request.getCurrentPositionSeconds());

        // Cap max watched position to avoid re-watching bugs
        if (request.getCurrentPositionSeconds() > progress.getMaxWatchedInSeconds()) {
            progress.setMaxWatchedInSeconds(request.getCurrentPositionSeconds());
        }

        // Mark completed if user watched >= 90% of the video
        if (!progress.isCompleted() && subTopic.getDurationInSeconds() > 0) {
            double watchedRatio =
                    (double) progress.getMaxWatchedInSeconds()
                            / subTopic.getDurationInSeconds();
            if (!progress.isCompleted()
                    && watchedRatio >= 0.90) {

                System.out.println(
                        "VIDEO COMPLETED => subTopic="
                                + subTopic.getId()
                );

                progress.setCompleted(true);
            }
        }

        videoProgressRepository.save(progress);

        // Update total course progress percentage in Enrollment
        recalculateCourseProgress(user.getId(), subTopic.getChapter().getCourse().getId());
    }

    private void recalculateCourseProgress(Long userId, Long courseId) {
        int totalSubTopics = subTopicRepository.countTotalSubTopicsByCourse(courseId);
        if (totalSubTopics == 0) return;

        int completedSubTopics = videoProgressRepository.countCompletedSubTopicsByCourse(userId, courseId);

        double percentage = Math.min(100.0, ((double) completedSubTopics / totalSubTopics) * 100.0);

        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment record not found"));

        enrollment.setProgressPercentage(Math.round(percentage * 100.0) / 100.0);
        enrollmentRepository.save(enrollment);
    }
}