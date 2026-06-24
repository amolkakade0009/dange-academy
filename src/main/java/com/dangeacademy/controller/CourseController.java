package com.dangeacademy.controller;

import com.dangeacademy.entity.Course;
import com.dangeacademy.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
/*
@CrossOrigin("*")
*/
public class CourseController {

    private final CourseService courseService;

    // Create Course
    @PostMapping(value = "/create",
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Course> createCourse(
            @Valid @RequestPart("course") Course course ,
            @RequestPart("videoFile")MultipartFile videoFile,
            @RequestPart("thumnail")MultipartFile thumnailFile) {

        Course savedCourse = courseService.createCourse(course, videoFile, thumnailFile);

        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    // Get All Courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {

        return ResponseEntity.ok(
                courseService.getAllCourses()
        );
    }

    // Get Course By Id
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {

        return ResponseEntity.ok(
                courseService.getCourseById(id)
        );
    }

    // Update Course
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody Course course) {

        return ResponseEntity.ok(
                courseService.updateCourse(id, course)
        );
    }

    // Delete Course
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseEntity.ok(
                "Course Deleted Successfully"
        );
    }


    @PostMapping("/{courseId}/thumbnail")
    public ResponseEntity<Course> uploadThumbnail(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                courseService.updateThumbnail(courseId, file)
        );
    }

    @PutMapping("/{courseId}/duration")
    public ResponseEntity<Course> updateDuration(
            @PathVariable Long courseId,
            @RequestParam Long durationInSeconds) {

        return ResponseEntity.ok(
                courseService.updateDuration(courseId, durationInSeconds)
        );
    }
}
