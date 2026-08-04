package com.dangeacademy.service;

import com.dangeacademy.dto.CourseRequestDto;
import com.dangeacademy.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {

    Course createCourse(CourseRequestDto course);

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course updateCourse(Long id, CourseRequestDto course);

    void deleteCourse(Long id);

    List<Course> getCoursesByMentor(Long mentorId);

    /*Course updateDuration(Long courseId, Long durationInSeconds);*/
}
