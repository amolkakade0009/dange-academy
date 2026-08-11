package com.dangeacademy.repository;

import com.dangeacademy.entity.SubTopic;
import com.dangeacademy.enums.VideoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubTopicRepository extends JpaRepository<SubTopic, Long> {

    List<SubTopic> findByChapterIdOrderByTopicOrderAsc(Long chapterId);
    List<SubTopic> findByVideoStatus(VideoStatus status);
    @Query("SELECT COUNT(st) FROM SubTopic st WHERE st.chapter.course.id = :courseId")
    int countTotalSubTopicsByCourse(@Param("courseId") Long courseId);


}
