package com.dangeacademy.service.impl;

import com.dangeacademy.dto.VideoUploadResponse;
import com.dangeacademy.entity.Chapter;
import com.dangeacademy.entity.SubTopic;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.ChapterRepository;
import com.dangeacademy.repository.SubTopicRepository;
import com.dangeacademy.service.SubTopicService;
import com.dangeacademy.service.AWSS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubTopicServiceImpl implements SubTopicService {

    private final SubTopicRepository subTopicRepository;
    private final ChapterRepository chapterRepository;
    private final AWSS3Service s3Service;


    @Override
    public SubTopic createSubTopic(
            Long chapterId,
            SubTopic subTopic,
            MultipartFile videoFile) {

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chapter not found with id : "
                                        + chapterId));

        VideoUploadResponse response =
                s3Service.VideoUploadToAWSS3(videoFile);

        subTopic.setVideoUrl(response.getVideoKey());

//        subTopic.setDurationInSeconds(
//                response.getDurationInSeconds()
//        );

        subTopic.setChapter(chapter);

        return subTopicRepository.save(subTopic);
    }

    @Override
    public SubTopic getSubTopicById(Long subTopicId) {

        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "SubTopic not found with id : "
                                                        + subTopicId));


        subTopic.setVideoUrl(s3Service.preSignedUrl(subTopic.getVideoUrl()));

        return  subTopic;
    }

    @Override
    public List<SubTopic> getSubTopicsByChapter(Long chapterId) {

        List<SubTopic> subTopic =  subTopicRepository
                            .findByChapterIdOrderByTopicOrderAsc(chapterId);

        return  subTopic.stream()
                .map(subTopic1 -> {
                    subTopic1.setVideoUrl(
                            s3Service.preSignedUrl(subTopic1.getVideoUrl())
                    );
                    return subTopic1;
                }).toList();
    }

    @Override
    public SubTopic updateSubTopic(
            Long subTopicId,
            SubTopic updatedSubTopic) {

        SubTopic subTopic = getSubTopicById(subTopicId);

        subTopic.setTopicName(updatedSubTopic.getTopicName());
        subTopic.setContent(updatedSubTopic.getContent());
        subTopic.setTopicOrder(updatedSubTopic.getTopicOrder());

        return subTopicRepository.save(subTopic);
    }

    @Override
    public void deleteSubTopic(Long subTopicId) {

        SubTopic subTopic = getSubTopicById(subTopicId);

        subTopicRepository.delete(subTopic);
    }

    @Override
    public String getVideoUrl(Long subTopicId) {

        SubTopic subTopic = getSubTopicById(subTopicId);
        subTopic.setVideoUrl(s3Service.preSignedUrl(subTopic.getVideoUrl()));

        return s3Service.preSignedUrl(subTopic.getVideoUrl());
    }
}