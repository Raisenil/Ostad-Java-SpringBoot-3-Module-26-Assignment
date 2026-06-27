package com.example.github_repo_reader.db.topic.repository;

import com.example.github_repo_reader.db.topic.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {

    @Query("SELECT t from Topic t ORDER BY t.serial")
    List<Topic> readSerially();

}
