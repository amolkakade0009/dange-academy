package com.dangeacademy.controller;

import com.dangeacademy.dto.EnrollmentResponseDto;
import com.dangeacademy.entity.Enrollment;
import com.dangeacademy.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // Enroll a student in a course
   /* @PostMapping("enrollStudent")
    public ResponseEntity<EnrollmentResponseDto> enrollStudent(
            @RequestBody Enrollment enrollment) {

        EnrollmentResponseDto response =
                enrollmentService.enrollStudent(enrollment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
*/
    // Get enrollment by ID
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<EnrollmentResponseDto> getEnrollment(
            @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(
                enrollmentService.getEnrollment(enrollmentId));
    }

    // Get all enrollments of a student
    @GetMapping("/enrollment/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDto>> getStudentEnrollments(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                enrollmentService.getStudentEnrollments(studentId));
    }

    // Get all students enrolled in a course
    @GetMapping("/admin/enrollment/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponseDto>> getCourseEnrollments(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                enrollmentService.getCourseEnrollments(courseId));
    }

    // Get a student's enrollment for a specific course
    @GetMapping("/student/enrollment/{studentId}/course/{courseId}")
    public ResponseEntity<EnrollmentResponseDto> getStudentCourseEnrollment(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                enrollmentService.getStudentCourseEnrollment(studentId, courseId));
    }

    // Check if a student is enrolled in a course
    @GetMapping("/student/enrollment/{studentId}/course/{courseId}/exists")
    public ResponseEntity<Boolean> isStudentEnrolled(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                enrollmentService.isStudentEnrolled(studentId, courseId));
    }

    // Delete an enrollment
    @DeleteMapping("/admin/enrollment/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable Long enrollmentId) {

        enrollmentService.deleteEnrollment(enrollmentId);

        return ResponseEntity.noContent().build();
    }
}