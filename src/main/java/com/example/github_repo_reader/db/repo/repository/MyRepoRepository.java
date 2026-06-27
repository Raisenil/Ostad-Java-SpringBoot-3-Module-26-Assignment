package com.example.github_repo_reader.db.repo.repository;

import com.example.github_repo_reader.db.repo.model.MyRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyRepoRepository extends JpaRepository<MyRepo, String> {

    @Query("SELECT mr from MyRepo mr ORDER BY mr.serial")
    List<MyRepo> readSerially();

}
