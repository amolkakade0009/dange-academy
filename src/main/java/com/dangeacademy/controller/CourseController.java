package com.dangeacademy.controller;

import com.dangeacademy.entity.Course;
import com.dangeacademy.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // Create Course
    @PostMapping("/admin/course/create")
    public ResponseEntity<Course> createCourse(
            @Valid @RequestBody Course course) {

        Course savedCourse = courseService.createCourse(course);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCourse);
    }

    @GetMapping("/public/courses")
    public ResponseEntity<List<Course>> getAllCoursesForAdmin() {

        List<Course> course = courseService.getAllCourses();
        if (course.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return ResponseEntity.ok(course);

        }

    }

    // Get All Courses
    @GetMapping("/student/courses")
    public ResponseEntity<List<Course>> getAllCourses() {

         List<Course> course = courseService.getAllCourses();
         if (course.isEmpty()){
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }else {
             return ResponseEntity.ok(course);

         }

    }

    // Get Course By Id
    @GetMapping("/student/course/{id}")
    public ResponseEntity<Course> getCourseById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                courseService.getCourseById(id)
        );
    }

    // Update Course
    @PutMapping("/admin/course/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody Course course) {

        return ResponseEntity.ok(
                courseService.updateCourse(id, course)
        );
    }

    // Delete Course
    @DeleteMapping("/admin/course/{id}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseEntity.ok("Course Deleted Successfully");
    }

    // Update Duration
//    @PutMapping("/{courseId}/duration")
//    public ResponseEntity<Course> updateDuration(
//            @PathVariable Long courseId,
//            @RequestParam Long durationInSeconds) {
//
//        return ResponseEntity.ok(
//                courseService.updateDuration(courseId, durationInSeconds)
//        );
//    }

}