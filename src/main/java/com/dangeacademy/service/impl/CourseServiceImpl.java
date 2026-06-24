package com.dangeacademy.service.impl;

import com.dangeacademy.dto.VideoUploadResponse;
import com.dangeacademy.entity.Chapter;
import com.dangeacademy.entity.Course;
import com.dangeacademy.exception.CourseNotFoundException;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.service.CourseService;
import com.dangeacademy.service.AWSS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final AWSS3Service s3Service;


    @Override
    public Course createCourse(Course course, MultipartFile videoFile, MultipartFile thumbnailFile) {
        VideoUploadResponse videoUploadResponse = s3Service.VideoUploadToAWSS3(videoFile);
        String thumnailKey = s3Service.ThumbnailUploadToAWSS3(thumbnailFile);

        course.setIntroVideoUrl(videoUploadResponse.getVideoKey());
        course.setThumbnailUrl(thumnailKey);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
         List<Course> updatedCourse = courses.stream().map(course -> {
             course.setIntroVideoUrl(s3Service.preSignedUrl(course.getIntroVideoUrl()));
             course.setThumbnailUrl(s3Service.preSignedUrl(course.getThumbnailUrl()));
             course.setChapters(addPreSignUrlInChapters(course.getChapters()));
             return course;
         }).toList();

         return  updatedCourse;

    }

    public List<Chapter> addPreSignUrlInChapters(List<Chapter> chapters){
             return  chapters.stream()
                            .map(chapter -> {
                                chapter.setSubTopics(
                                        chapter.getSubTopics()
                                                .stream()
                                                .map(subTopic -> {
                                                            subTopic.setVideoUrl(
                                                                    s3Service.preSignedUrl(
                                                                            subTopic.getVideoUrl()
                                                                    )
                                                            );
                                                            return subTopic;
                                                        }
                                                )
                                                .toList()
                                );
                                return chapter;
                            })
                            .toList();
    }

    @Override
    public Course getCourseById(Long id) {
        Optional<Course> course =  courseRepository.findById(id);
        course.get().setIntroVideoUrl(s3Service.preSignedUrl(course.get().getIntroVideoUrl()));
        course.get().setThumbnailUrl(s3Service.preSignedUrl(course.get().getThumbnailUrl()));
        course.get().setChapters(addPreSignUrlInChapters(course.get().getChapters()));
        return course.orElseThrow(() ->
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
        /*existingCourse.setVideoUrl(course.getVideoUrl());*/
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

    @Override
    public Course updateThumbnail(Long courseId, MultipartFile thumbnail) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course not found with id : " + courseId));

        // Upload to S3
        String thumbnailUrl = s3Service.uploadFile(thumbnail);

        course.setThumbnailUrl(thumbnailUrl);

        return courseRepository.save(course);
    }

    @Override
    public Course updateDuration(Long courseId, Long durationInSeconds) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course not found with id : " + courseId));

        course.setDurationInSeconds(durationInSeconds);

        return courseRepository.save(course);
    }

}
