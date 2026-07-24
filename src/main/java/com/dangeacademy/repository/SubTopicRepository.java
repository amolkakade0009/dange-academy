package com.dangeacademy.repository;

import com.dangeacademy.entity.SubTopic;
import com.dangeacademy.entity.VideoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubTopicRepository extends JpaRepository<SubTopic, Long> {

    List<SubTopic> findByChapterIdOrderByTopicOrderAsc(Long chapterId);
    List<SubTopic> findByVideoStatus(VideoStatus status);


}
