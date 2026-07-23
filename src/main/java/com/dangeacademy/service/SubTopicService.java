package com.dangeacademy.service;

import com.dangeacademy.entity.SubTopic;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SubTopicService {

    SubTopic createSubTopic(Long chapterId, SubTopic subTopic);
    SubTopic getSubTopicById(Long subTopicId);

    List<SubTopic> getSubTopicsByChapter(Long chapterId);

    SubTopic updateSubTopic(Long subTopicId, SubTopic subTopic);

    void deleteSubTopic(Long subTopicId);

}