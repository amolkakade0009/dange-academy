package com.dangeacademy.service.impl;

import com.dangeacademy.entity.Course;
import com.dangeacademy.exception.CourseNotFoundException;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Course createCourse(Course course) {

        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {

        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {

        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course Not Found With Id : " + id
                        ));
    }

    @Override
    public Course updateCourse(Long id, Course course) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course Not Found With Id : " + id
                        ));

        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setFees(course.getFees());
        existingCourse.setVideoUrl(course.getVideoUrl());
        existingCourse.setStatus(course.getStatus());

        return courseRepository.save(existingCourse);
    }

    @Override
    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course Not Found With Id : " + id
                        ));

        courseRepository.delete(course);
    }

}
