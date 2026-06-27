package com.example.github_repo_reader;

import com.example.github_repo_reader.schedular.PeriodicScheduler;
import com.example.github_repo_reader.db.topic.service.TopicService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GithubRepoReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubRepoReaderApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PeriodicScheduler scheduler, TopicService topicService) {
        return args -> {
            if (topicService.readSerially().isEmpty()) {
                new Thread(() -> {
                    try {
                        scheduler.updateRepoAndContributors();
                        scheduler.updateCodeBase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        };
    }
}
