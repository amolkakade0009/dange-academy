package com.dangeacademy.service;

import com.dangeacademy.dto.EnrollmentResponseDto;
import com.dangeacademy.entity.Enrollment;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDto enrollStudent(Enrollment enrollment);

    EnrollmentResponseDto getEnrollment(Long enrollmentId);

    List<EnrollmentResponseDto> getStudentEnrollments(Long studentId);

    List<EnrollmentResponseDto> getCourseEnrollments(Long courseId);

    EnrollmentResponseDto getStudentCourseEnrollment(Long studentId, Long courseId);

    boolean isStudentEnrolled(Long studentId, Long courseId);

    void deleteEnrollment(Long enrollmentId);
}