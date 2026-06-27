package com.example.github_repo_reader.db.contribution.service;

import com.example.github_repo_reader.db.contribution.dto.ContributionDTO;
import com.example.github_repo_reader.db.contribution.model.Contribution;
import com.example.github_repo_reader.db.contribution.repository.ContributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository repository;

    public String saveAll(List<Contribution> contributions){
        int errorCount = 0;

        for(Contribution item : contributions){
            final String error = save(item);
            if(error != null) errorCount++;
        }

        if(errorCount == 0) return null;

        return "Error at saving " + errorCount +" repo info";
    }

    public String save(Contribution item){
        try{
            repository.save(item);
            return null;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public List<ContributionDTO> readSeriallyByRepoName(String repoName){
        return repository.findAllAsDTO(repoName);
    }

    public String deleteAll(){
        try {
            repository.deleteAll();
            return null;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

}
