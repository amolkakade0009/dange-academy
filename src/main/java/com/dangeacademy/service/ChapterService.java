package com.dangeacademy.service;

import com.dangeacademy.entity.Chapter;

import java.util.List;

public interface ChapterService {

    Chapter createChapter(Long courseId, Chapter chapter);

    Chapter getChapterById(Long chapterId);

    List<Chapter> getAllChapters();

    List<Chapter> getChaptersByCourse(Long courseId);

    Chapter updateChapter(Long chapterId, Chapter chapter);

    void deleteChapter(Long chapterId);
}