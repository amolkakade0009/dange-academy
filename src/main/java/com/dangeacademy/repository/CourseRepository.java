package com.dangeacademy.repository;

import com.dangeacademy.entity.Course;
import com.dangeacademy.entity.VideoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByIntroVideoStatus(VideoStatus introVideoStatus);
}