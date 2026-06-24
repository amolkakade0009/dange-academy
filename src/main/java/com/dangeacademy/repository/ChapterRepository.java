package com.dangeacademy.repository;

import com.dangeacademy.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByCourseIdOrderByChapterOrderAsc(Long courseId);
}