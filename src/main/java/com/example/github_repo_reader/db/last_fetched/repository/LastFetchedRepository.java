package com.example.github_repo_reader.db.last_fetched.repository;

import com.example.github_repo_reader.db.last_fetched.model.LastFetched;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LastFetchedRepository extends JpaRepository<LastFetched, Long> {

    Optional<LastFetched> findTopByOrderByTimestampDesc();

}
