package com.dangeacademy.controller;

import com.dangeacademy.entity.SubTopic;
import com.dangeacademy.service.SubTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subtopics")
@RequiredArgsConstructor
public class SubTopicController {

    private final SubTopicService subTopicService;

    // Create SubTopic
    @PostMapping("/chapter/{chapterId}")
    public ResponseEntity<SubTopic> createSubTopic(
            @PathVariable Long chapterId,
            @Valid @RequestBody SubTopic subTopic) {

        SubTopic savedSubTopic =
                subTopicService.createSubTopic(chapterId, subTopic);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedSubTopic);
    }

    // Get SubTopic By Id
    @GetMapping("/{subTopicId}")
    public ResponseEntity<SubTopic> getSubTopicById(
            @PathVariable Long subTopicId) {

        return ResponseEntity.ok(
                subTopicService.getSubTopicById(subTopicId)
        );
    }

    // Get All SubTopics Of Chapter
    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<SubTopic>> getSubTopicsByChapter(
            @PathVariable Long chapterId) {

        return ResponseEntity.ok(
                subTopicService.getSubTopicsByChapter(chapterId)
        );
    }

    // Update SubTopic
    @PutMapping("/{subTopicId}")
    public ResponseEntity<SubTopic> updateSubTopic(
            @PathVariable Long subTopicId,
            @Valid @RequestBody SubTopic subTopic) {

        return ResponseEntity.ok(
                subTopicService.updateSubTopic(subTopicId, subTopic)
        );
    }

    // Delete SubTopic
    @DeleteMapping("/{subTopicId}")
    public ResponseEntity<String> deleteSubTopic(
            @PathVariable Long subTopicId) {

        subTopicService.deleteSubTopic(subTopicId);

        return ResponseEntity.ok("SubTopic deleted successfully");
    }

}