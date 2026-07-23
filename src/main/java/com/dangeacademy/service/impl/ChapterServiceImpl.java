package com.dangeacademy.service.impl;

import com.dangeacademy.entity.Chapter;
import com.dangeacademy.entity.Course;
import com.dangeacademy.entity.SubTopic;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.ChapterRepository;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.repository.SubTopicRepository;
import com.dangeacademy.service.ChapterService;
import com.dangeacademy.service.SubTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private  final SubTopicRepository subTopicRepository;
    private final SubTopicService subTopicService;


    @Override
    public Chapter createChapter(Long courseId, Chapter chapter) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : " + courseId));


       chapter.setCourse(course);
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter getChapterById(Long chapterId) {

        return chapterRepository.findById(chapterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chapter not found with id : " + chapterId));
    }

    @Override
    public List<Chapter> getAllChapters() {

        return chapterRepository.findAll();
    }


    @Override
    public List<Chapter> getChaptersByCourse(Long courseId) {

        courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : " + courseId));

        return chapterRepository.findByCourseIdOrderByChapterOrderAsc(courseId);
    }


    @Override
    public Chapter updateChapter(Long chapterId, Chapter updatedChapter) {

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chapter not found with id : " + chapterId));

        chapter.setChapterName(updatedChapter.getChapterName());
        chapter.setDescription(updatedChapter.getDescription());
        chapter.setChapterOrder(updatedChapter.getChapterOrder());

        return chapterRepository.save(chapter);
    }

    @Override
    public void deleteChapter(Long chapterId) {

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chapter not found with id : " + chapterId));

        List<SubTopic> subTopics =
                subTopicRepository.findByChapterIdOrderByTopicOrderAsc(chapterId);

        subTopics.forEach(subTopic ->
                subTopicService.deleteSubTopic(subTopic.getId())
        );

        chapterRepository.delete(chapter);
    }
}