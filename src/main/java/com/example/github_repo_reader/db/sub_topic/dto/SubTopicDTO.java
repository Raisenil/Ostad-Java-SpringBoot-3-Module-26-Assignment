package com.example.github_repo_reader.db.sub_topic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SubTopicDTO {

    @JsonProperty("sub_topic_name")
    private final String subTopicName;
}
