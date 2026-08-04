package com.dangeacademy.service;

import com.dangeacademy.entity.Mentor;

import java.util.List;

public interface MentorService {

    Mentor createMentor(Mentor mentor);

    Mentor updateMentor(Long id, Mentor mentor);

    Mentor getMentorById(Long id);

    List<Mentor> getAllMentors();

    void deleteMentor(Long id);
}