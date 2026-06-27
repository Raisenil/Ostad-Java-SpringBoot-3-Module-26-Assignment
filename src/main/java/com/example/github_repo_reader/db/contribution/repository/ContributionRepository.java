package com.example.github_repo_reader.db.contribution.repository;

import com.example.github_repo_reader.db.contribution.dto.ContributionDTO;
import com.example.github_repo_reader.db.contribution.model.Contribution;
import com.example.github_repo_reader.db.contribution.model.ContributionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, ContributionPK> {

    @Query(
            "SELECT new com.example.github_repo_reader.db.contribution.dto.ContributionDTO(" +
            "c.contributionPK.userName, " +
            "c.contributionCount, " +
            "c.profileUrl)" +
            "FROM Contribution c WHERE c.contributionPK.repoName = :repoName ORDER BY c.contributionCount DESC")
    List<ContributionDTO> findAllAsDTO(@Param("repoName") String repoName);

}
