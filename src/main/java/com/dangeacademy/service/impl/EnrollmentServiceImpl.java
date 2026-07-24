package com.dangeacademy.service.impl;

import com.dangeacademy.dto.EnrollmentResponseDto;
import com.dangeacademy.dto.UserResponseDto;
import com.dangeacademy.entity.Enrollment;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.EnrollmentRepository;
import com.dangeacademy.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public EnrollmentResponseDto enrollStudent(Enrollment enrollment) {

        if (enrollmentRepository.existsByStudent_IdAndCourse_Id(
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId())) {

            throw new RuntimeException("Student is already enrolled in this course.");
        }

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return mapToResponse(savedEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponseDto getEnrollment(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enrollment not found with id : " + enrollmentId));

        return mapToResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDto> getStudentEnrollments(Long studentId) {

        return enrollmentRepository.findByStudent_Id(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDto> getCourseEnrollments(Long courseId) {

        return enrollmentRepository.findByCourse_Id(courseId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponseDto getStudentCourseEnrollment(Long studentId, Long courseId) {

        Enrollment enrollment = enrollmentRepository
                .findByStudent_IdAndCourse_Id(studentId, courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found."));

        return mapToResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Long studentId, Long courseId) {

        return enrollmentRepository.existsByStudent_IdAndCourse_Id(studentId, courseId);
    }

    @Override
    public void deleteEnrollment(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enrollment not found with id : " + enrollmentId));

        enrollmentRepository.delete(enrollment);
    }

    private EnrollmentResponseDto mapToResponse(Enrollment enrollment) {

        UserResponseDto studentDto = UserResponseDto.builder()
                .id(enrollment.getStudent().getId())
                .name(enrollment.getStudent().getName())
                .email(enrollment.getStudent().getEmail())
                .mobileNumber(enrollment.getStudent().getMobileNumber())
                .role(enrollment.getStudent().getRole())
                .build();

        return EnrollmentResponseDto.builder()
                .id(enrollment.getId())
                .student(studentDto)
                .course(enrollment.getCourse())
                .razorpayOrderId(enrollment.getRazorpayOrderId())
                .razorpayPaymentId(enrollment.getRazorpayPaymentId())
                .enrolledAt(enrollment.getEnrolledAt())
                .expireOn(enrollment.getExpireOn())
                .build();
    }
}