package com.example.github_repo_reader.db.blog.repository;

import com.example.github_repo_reader.db.blog.model.Blog;
import com.example.github_repo_reader.db.blog.model.BlogPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, BlogPK> {

}
