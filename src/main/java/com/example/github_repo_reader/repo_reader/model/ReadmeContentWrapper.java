package com.example.github_repo_reader.repo_reader.model;

import java.util.Map;

public record ReadmeContentWrapper(ReadmeContent readmeContent, Map<String, ReadmeContent> topicContentMap) {
}
