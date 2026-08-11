package com.dangeacademy.repository;

import com.dangeacademy.entity.VideoProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
/**
 * @author Rohan Ghuge
 * @since 09-08-2026
 */


public interface VideoProgressRepository extends JpaRepository<VideoProgress, Long> {

    Optional<VideoProgress> findByUserIdAndSubTopicId(Long userId, Long subTopicId);

    @Query("SELECT COUNT(vp) FROM VideoProgress vp " +
            "WHERE vp.user.id = :userId " +
            "AND vp.subTopic.chapter.course.id = :courseId " +
            "AND vp.isCompleted = true")
    int countCompletedSubTopicsByCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT vp.subTopic.id FROM VideoProgress vp " +
            "WHERE vp.user.id = :userId " +
            "AND vp.subTopic.chapter.course.id = :courseId " +
            "AND vp.isCompleted = true")
    List<Long> findCompletedSubTopicIds(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
