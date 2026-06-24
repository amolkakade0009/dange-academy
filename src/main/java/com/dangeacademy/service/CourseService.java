package com.dangeacademy.service;

import com.dangeacademy.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {

    Course createCourse(Course course, MultipartFile videoFile, MultipartFile thumnailFile);

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course updateCourse(Long id, Course course);

    void deleteCourse(Long id);

    Course updateThumbnail(Long courseId, MultipartFile thumbnail);

    Course updateDuration(Long courseId, Long durationInSeconds);
}
