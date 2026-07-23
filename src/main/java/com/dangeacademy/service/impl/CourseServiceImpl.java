package com.dangeacademy.service.impl;

import com.dangeacademy.client.CloudflareClient;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareVideoStatusResponse;
import com.dangeacademy.entity.Course;
import com.dangeacademy.exception.CourseNotFoundException;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CloudflareClient cloudflareClient;

    @Override
    public Course createCourse(Course course) {

        CloudflareVideoStatusResponse response =
                cloudflareClient.getVideoDetails(course.getIntroVideoUid());

        if (!response.isReady()) {
            throw new IllegalStateException(
                    "Video is still processing. Please try again later."
            );
        }

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
        existingCourse.setPrice(course.getPrice());
        existingCourse.setStatus(course.getStatus());
        existingCourse.setIntroVideoUid(course.getIntroVideoUid());

        return courseRepository.save(existingCourse);
    }

    @Override
    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course Not Found With Id : " + id
                        ));

        if (course.getIntroVideoUid() != null
                && !course.getIntroVideoUid().isBlank()) {

            cloudflareClient.deleteVideo(course.getIntroVideoUid());

        }

        courseRepository.delete(course);
    }

    @Override
    public Course updateDuration(Long courseId, Long durationInSeconds) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : " + courseId
                        ));

        course.setDurationInSeconds(durationInSeconds);

        return courseRepository.save(course);
    }

}