package com.dangeacademy.service.impl;

import com.dangeacademy.entity.Mentor;
import com.dangeacademy.exception.MentorNotFoundException;
import com.dangeacademy.exception.ResourceAlreadyExistsException;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.MentorRepository;
import com.dangeacademy.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    @Override
    public Mentor createMentor(Mentor mentor) {

        if (mentorRepository.existsByEmail(mentor.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (mentorRepository.existsByMobileNumber(mentor.getMobileNumber())) {
            throw new ResourceAlreadyExistsException("Mobile number already exists");
        }

        return mentorRepository.save(mentor);
    }

    @Override
    public Mentor updateMentor(Long id, Mentor mentor) {

        Mentor existingMentor = mentorRepository.findById(id)
                .orElseThrow(() ->
                        new MentorNotFoundException(
                                "Mentor not found with id : " + id));

        existingMentor.setName(mentor.getName());
        existingMentor.setEmail(mentor.getEmail());
        existingMentor.setMobileNumber(mentor.getMobileNumber());
        existingMentor.setProfileImageUrl(mentor.getProfileImageUrl());
        existingMentor.setBio(mentor.getBio());
        existingMentor.setDesignation(mentor.getDesignation());
        existingMentor.setExperienceYears(mentor.getExperienceYears());
        existingMentor.setQualification(mentor.getQualification());

        return mentorRepository.save(existingMentor);
    }


    @Override
    public Mentor getMentorById(Long id) {

        return mentorRepository.findById(id)
                .orElseThrow(() ->
                        new MentorNotFoundException(
                                "Mentor not found with id : " + id));
    }

    @Override
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    @Override
    public void deleteMentor(Long id) {

        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() ->
                        new MentorNotFoundException(
                                "Mentor not found with id : " + id));

        mentorRepository.delete(mentor);
    }
}