package com.dangeacademy.service.impl;

import com.dangeacademy.client.CloudflareClient;
import com.dangeacademy.dto.CourseRequestDto;
import com.dangeacademy.entity.Chapter;
import com.dangeacademy.entity.Course;
import com.dangeacademy.entity.Mentor;
import com.dangeacademy.enums.VideoStatus;
import com.dangeacademy.exception.CourseNotFoundException;
import com.dangeacademy.exception.MentorNotFoundException;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.repository.MentorRepository;
import com.dangeacademy.service.ChapterService;
import com.dangeacademy.service.CourseService;
import com.dangeacademy.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CloudflareClient cloudflareClient;
    private final ChapterService chapterService;
    private final MentorService mentorService;
    private final MentorRepository mentorRepository;


    @Override
    public Course createCourse(CourseRequestDto courseRequestDto) {

        Mentor mentor = mentorService.getMentorById(courseRequestDto.getMentorId());

        Course course = mapToCourse(courseRequestDto,mentor);
        course.setIntroVideoStatus(VideoStatus.PROCESSING);

        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {

        return courseRepository.findAll(Sort.by(Sort.Direction.DESC  , "uploadedDate"));

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
    public List<Course> getCoursesByMentor(Long mentorId) {

        mentorRepository.findById(mentorId)
                .orElseThrow(() ->
                        new MentorNotFoundException(
                                "Mentor not found with id : " + mentorId));

        return courseRepository.findByMentorId(mentorId);
    }


    @Override
    public Course updateCourse(Long id, CourseRequestDto course) {

        Mentor mentor = mentorService.getMentorById(course.getMentorId());

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course Not Found With Id : " + id
                        ));

        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setPrice(course.getPrice());
        existingCourse.setCourseValidity(course.getCourseValidity());
        existingCourse.setOriginalPrice(course.getOriginalPrice());
        existingCourse.setMentor(mentor);
        existingCourse.setCourseThumbnailUrl(course.getCourseThumbnailUrl());
        existingCourse.setCategory(course.getCategory());
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

        List<Chapter> chapterList = course.getChapters();

        chapterList.forEach(chapter -> chapterService.deleteChapter(chapter.getId()));

        if (course.getIntroVideoUid() != null
                && !course.getIntroVideoUid().isBlank()) {
            try{
                cloudflareClient.deleteVideo(course.getIntroVideoUid());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        courseRepository.delete(course);
    }

    private Course mapToCourse(CourseRequestDto dto, Mentor mentor) {

        return Course.builder()
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .originalPrice(dto.getOriginalPrice())
                .mentor(mentor)
                .courseThumbnailUrl(dto.getCourseThumbnailUrl())
                .category(dto.getCategory())
                .introVideoUid(dto.getIntroVideoUid())
                .introVideoStatus(VideoStatus.PROCESSING)
                .courseValidity(dto.getCourseValidity())
                .status(dto.getStatus())
                .build();
    }


    /*@Override
    public Course updateDuration(Long courseId, Long durationInSeconds) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : " + courseId
                        ));

        course.setDurationInSeconds(durationInSeconds);

        return courseRepository.save(course);
    }*/

}