package com.dangeacademy.controller;

import com.dangeacademy.entity.Mentor;
import com.dangeacademy.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @PostMapping("/create")
    public ResponseEntity<Mentor> createMentor(@RequestBody Mentor mentor) {

        Mentor savedMentor = mentorService.createMentor(mentor);

        return new ResponseEntity<>(savedMentor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mentor> updateMentor(@PathVariable Long id,
                                               @RequestBody Mentor mentor) {

        Mentor updatedMentor = mentorService.updateMentor(id, mentor);

        return ResponseEntity.ok(updatedMentor);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Mentor> getMentorById(@PathVariable Long id) {

        return ResponseEntity.ok(mentorService.getMentorById(id));
    }

    @GetMapping
    public ResponseEntity<List<Mentor>> getAllMentors() {

        return ResponseEntity.ok(mentorService.getAllMentors());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMentor(@PathVariable Long id) {

        mentorService.deleteMentor(id);

        return ResponseEntity.ok("Mentor deleted successfully.");
    }
}