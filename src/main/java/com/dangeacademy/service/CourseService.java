package com.dangeacademy.service;

import com.dangeacademy.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {

    Course createCourse(Course course);

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course updateCourse(Long id, Course course);

    void deleteCourse(Long id);

    /*Course updateDuration(Long courseId, Long durationInSeconds);*/
}
