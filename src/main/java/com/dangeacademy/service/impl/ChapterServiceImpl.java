package com.dangeacademy.service.impl;

import com.dangeacademy.entity.Chapter;
import com.dangeacademy.entity.Course;
import com.dangeacademy.exception.CourseNotFoundException;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.ChapterRepository;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.service.AWSS3Service;
import com.dangeacademy.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private  final AWSS3Service s3Service;

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
       Chapter chapter =  chapterRepository.findById(chapterId)
               .orElseThrow(()-> new CourseNotFoundException("Chapter not found with id : " + chapterId));

       chapter.setSubTopics(
               chapter.getSubTopics()
                   .stream()
                   .map(subTopic -> {
                        subTopic.setVideoUrl(
                            s3Service.preSignedUrl(subTopic.getVideoUrl())
                        );
                        return subTopic;
                   })
                   .toList()
               );

       return chapter;
    }

    @Override
    public List<Chapter> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();

        return chapters.stream()
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
    public List<Chapter> getChaptersByCourse(Long courseId) {

        courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : " + courseId));

        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByChapterOrderAsc(courseId);
        return chapters.stream()
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

        chapterRepository.delete(chapter);
    }
}