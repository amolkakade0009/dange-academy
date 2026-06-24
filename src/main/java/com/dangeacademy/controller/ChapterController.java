package com.dangeacademy.controller;

import com.dangeacademy.entity.Chapter;
import com.dangeacademy.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping("/course/{courseId}")
    public ResponseEntity<Chapter> createChapter(
            @PathVariable Long courseId,
            @RequestBody Chapter chapter) {

        return new ResponseEntity<>(
                chapterService.createChapter(courseId, chapter),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<Chapter> getChapterById(
            @PathVariable Long chapterId) {

        return ResponseEntity.ok(
                chapterService.getChapterById(chapterId)
        );
    }

    @GetMapping
    public ResponseEntity<List<Chapter>> getAllChapters() {

        return ResponseEntity.ok(
                chapterService.getAllChapters()
        );
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Chapter>> getChaptersByCourse(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                chapterService.getChaptersByCourse(courseId)
        );
    }

    @PutMapping("/{chapterId}")
    public ResponseEntity<Chapter> updateChapter(
            @PathVariable Long chapterId,
            @RequestBody Chapter chapter) {

        return ResponseEntity.ok(
                chapterService.updateChapter(chapterId, chapter)
        );
    }

    @DeleteMapping("/{chapterId}")
    public ResponseEntity<String> deleteChapter(
            @PathVariable Long chapterId) {

        chapterService.deleteChapter(chapterId);

        return ResponseEntity.ok("Chapter deleted successfully");
    }
}