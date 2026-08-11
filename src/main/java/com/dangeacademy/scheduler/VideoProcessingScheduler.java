package com.dangeacademy.scheduler;

import com.dangeacademy.client.CloudflareClient;
import com.dangeacademy.config.cloudflare.cloudflaredto.CloudflareVideoStatusResponse;
import com.dangeacademy.entity.Course;
import com.dangeacademy.entity.SubTopic;
import com.dangeacademy.enums.VideoStatus;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.repository.SubTopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoProcessingScheduler {

    private final CourseRepository courseRepository;
    private final SubTopicRepository subTopicRepository;
    private final CloudflareClient cloudflareClient;

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void processVideos() {

        processCourseVideos();

        processSubTopicVideos();
    }

    private void processCourseVideos() {

        List<Course> courses =
                courseRepository.findByIntroVideoStatus(VideoStatus.PROCESSING);

        for (Course course : courses) {

            try {

                CloudflareVideoStatusResponse response =
                        cloudflareClient.getVideoDetails(course.getIntroVideoUid());

                if (response.isReady()) {

                    course.setIntroVideoStatus(VideoStatus.READY);

                    courseRepository.save(course);

                    log.info("Course video ready : {}", course.getId());
                }

            } catch (Exception e) {

                log.error("Course video check failed : {}", course.getId(), e);
            }
        }
    }

    private void processSubTopicVideos() {

        List<SubTopic> subTopics =
                subTopicRepository.findByVideoStatus(VideoStatus.PROCESSING);

        for (SubTopic subTopic : subTopics) {

            try {

                CloudflareVideoStatusResponse response =
                        cloudflareClient.getVideoDetails(subTopic.getVideoUid());

                if (response.isReady()) {

                    subTopic.setVideoStatus(VideoStatus.READY);
                    subTopic.setDurationInSeconds(response.getDuration());

                    subTopicRepository.save(subTopic);

                    log.info("SubTopic video ready : {}", subTopic.getId());
                }

            } catch (Exception e) {

                log.error("SubTopic video check failed : {}", subTopic.getId(), e);
            }
        }
    }
}