package com.example.github_repo_reader.index.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.github_repo_reader.db.sub_topic.dto.SubTopicDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class TopicWithSubTopic {

    @JsonProperty("topic_name")
    private final String topicName;

    @JsonProperty("no_of_sub_topics")
    private final Integer noOfSubTopics;

    private final List<SubTopicDTO> subTopicList;

    @Override
    public String toString() {
        return topicName+": "+subTopicList.size()+" sub topics";
    }
}
