package com.dangeacademy.controller;

import com.dangeacademy.dto.SaveProgressRequest;
import com.dangeacademy.entity.Enrollment;
import com.dangeacademy.entity.User;
import com.dangeacademy.entity.VideoProgress;
import com.dangeacademy.repository.EnrollmentRepository;
import com.dangeacademy.repository.VideoProgressRepository;
import com.dangeacademy.service.CourseProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Rohan Ghuge
 * @since 09-08-2026
 */
@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final CourseProgressService progressService;
    private final VideoProgressRepository videoProgressRepository;
    private final EnrollmentRepository enrollmentRepository; // Inject EnrollmentRepository

    @PostMapping("/sync")
    public ResponseEntity<Void> syncProgress(
            @AuthenticationPrincipal User user,
            @RequestBody SaveProgressRequest request) {

        progressService.saveVideoProgress(user, request);
        return ResponseEntity.ok().build();
    }

    // Endpoint to retrieve resume position for the React Player
    @GetMapping("/subtopic/{subTopicId}")
    public ResponseEntity<Map<String, Object>> getSubTopicProgress(
            @AuthenticationPrincipal User user,
            @PathVariable Long subTopicId) {

        VideoProgress progress = videoProgressRepository
                .findByUserIdAndSubTopicId(user.getId(), subTopicId)
                .orElse(null);

        double lastPosition = (progress != null) ? progress.getLastPositionInSeconds() : 0.0;
        boolean isCompleted = (progress != null) && progress.isCompleted();

        return ResponseEntity.ok(Map.of(
                "subTopicId", subTopicId,
                "lastPositionInSeconds", lastPosition,
                "isCompleted", isCompleted
        ));
    }

    // =========================================================================
    // ADD THIS MISSING ENDPOINT FOR COURSE PROGRESS SUMMARY & GREEN CHECKMARKS
    // =========================================================================
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseProgressSummary(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId) {

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // 1. Get cached percentage from Enrollment table
        double progressPercentage = enrollmentRepository
                .findByStudentIdAndCourseId(user.getId(), courseId)
                .map(Enrollment::getProgressPercentage)
                .orElse(0.0);

        // 2. Get list of completed subTopic IDs for green checkmarks
        List<Long> completedTopicIds = videoProgressRepository.findCompletedSubTopicIds(user.getId(), courseId);

        return ResponseEntity.ok(Map.of(
                "progressPercentage", progressPercentage,
                "completedSubTopicIds", completedTopicIds != null ? completedTopicIds : List.of()
        ));
    }
}