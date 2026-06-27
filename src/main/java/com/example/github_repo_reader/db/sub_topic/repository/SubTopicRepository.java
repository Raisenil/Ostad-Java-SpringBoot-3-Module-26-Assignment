package com.example.github_repo_reader.db.sub_topic.repository;

import com.example.github_repo_reader.db.blog.model.BlogPK;
import com.example.github_repo_reader.db.sub_topic.model.SubTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTopicRepository extends JpaRepository<SubTopic, BlogPK> {

    @Query("SELECT st FROM SubTopic st ORDER by st.serial")
    List<SubTopic> readSerially();

}
