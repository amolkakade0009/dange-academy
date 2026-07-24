package com.dangeacademy.service.impl;

import com.dangeacademy.client.CloudflareClient;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareVideoStatusResponse;
import com.dangeacademy.entity.Chapter;
import com.dangeacademy.entity.SubTopic;
import com.dangeacademy.entity.VideoStatus;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.ChapterRepository;
import com.dangeacademy.repository.SubTopicRepository;
import com.dangeacademy.service.SubTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubTopicServiceImpl implements SubTopicService {

    private final SubTopicRepository subTopicRepository;
    private final ChapterRepository chapterRepository;
    private final CloudflareClient cloudflareClient;

    @Override
    public SubTopic createSubTopic(
            Long chapterId,
            SubTopic subTopic) {

        CloudflareVideoStatusResponse response = cloudflareClient.getVideoDetails(subTopic.getVideoUid());

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chapter not found with id : " + chapterId));

        subTopic.setChapter(chapter);
        subTopic.setVideoStatus(VideoStatus.PROCESSING);
        subTopic.setDurationInSeconds(response.getDuration());

        return subTopicRepository.save(subTopic);
    }

    @Override
    public SubTopic getSubTopicById(Long subTopicId) {

        return subTopicRepository.findById(subTopicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "SubTopic not found with id : " + subTopicId));
    }

    @Override
    public List<SubTopic> getSubTopicsByChapter(Long chapterId) {

        return subTopicRepository
                .findByChapterIdOrderByTopicOrderAsc(chapterId);
    }

    @Override
    public SubTopic updateSubTopic(
            Long subTopicId,
            SubTopic updatedSubTopic) {

        SubTopic subTopic = getSubTopicById(subTopicId);

        subTopic.setTopicName(updatedSubTopic.getTopicName());
        subTopic.setContent(updatedSubTopic.getContent());
        subTopic.setTopicOrder(updatedSubTopic.getTopicOrder());
        subTopic.setVideoUid(updatedSubTopic.getVideoUid());
/*
        subTopic.setDurationInSeconds(updatedSubTopic.getDurationInSeconds());
*/

        return subTopicRepository.save(subTopic);
    }

    @Override
    public void deleteSubTopic(Long subTopicId) {

        SubTopic subTopic = getSubTopicById(subTopicId);

        if (subTopic.getVideoUid() != null
                && !subTopic.getVideoUid().isBlank()) {

            cloudflareClient.deleteVideo(subTopic.getVideoUid());
        }

        subTopicRepository.delete(subTopic);
    }

}