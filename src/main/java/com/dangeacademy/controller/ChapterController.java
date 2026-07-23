package com.dangeacademy.controller;

import com.dangeacademy.entity.Chapter;
import com.dangeacademy.service.ChapterService;
import jakarta.validation.Valid;
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

    // Create Chapter
    @PostMapping("/course/{courseId}")
    public ResponseEntity<Chapter> createChapter(
            @PathVariable Long courseId,
            @Valid @RequestBody Chapter chapter) {

        Chapter savedChapter = chapterService.createChapter(courseId, chapter);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedChapter);
    }

    // Get Chapter By Id
    @GetMapping("/{chapterId}")
    public ResponseEntity<Chapter> getChapterById(
            @PathVariable Long chapterId) {

        return ResponseEntity.ok(
                chapterService.getChapterById(chapterId)
        );
    }

    // Get All Chapters
    @GetMapping
    public ResponseEntity<List<Chapter>> getAllChapters() {

        return ResponseEntity.ok(
                chapterService.getAllChapters()
        );
    }

    // Get Chapters By Course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Chapter>> getChaptersByCourse(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                chapterService.getChaptersByCourse(courseId)
        );
    }

    // Update Chapter
    @PutMapping("/{chapterId}")
    public ResponseEntity<Chapter> updateChapter(
            @PathVariable Long chapterId,
            @Valid @RequestBody Chapter chapter) {

        return ResponseEntity.ok(
                chapterService.updateChapter(chapterId, chapter)
        );
    }

    // Delete Chapter
    @DeleteMapping("/{chapterId}")
    public ResponseEntity<String> deleteChapter(
            @PathVariable Long chapterId) {

        chapterService.deleteChapter(chapterId);

        return ResponseEntity.ok("Chapter deleted successfully");
    }
}