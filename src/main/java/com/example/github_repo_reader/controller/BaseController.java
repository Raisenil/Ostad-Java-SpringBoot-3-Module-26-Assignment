package com.example.github_repo_reader.controller;

import com.example.github_repo_reader.db.blog.dto.BlogDTO;
import com.example.github_repo_reader.db.blog.model.Blog;
import com.example.github_repo_reader.db.blog.model.BlogPK;
import com.example.github_repo_reader.db.blog.service.BlogService;
import com.example.github_repo_reader.db.contribution.dto.ContributionDTO;
import com.example.github_repo_reader.db.contribution.service.ContributionService;
import com.example.github_repo_reader.db.last_fetched.model.LastFetched;
import com.example.github_repo_reader.db.last_fetched.service.LastFetchedService;
import com.example.github_repo_reader.db.repo.dto.MyRepoDTO;
import com.example.github_repo_reader.db.repo.model.MyRepo;
import com.example.github_repo_reader.db.repo.service.MyRepoService;
import com.example.github_repo_reader.db.sub_topic.dto.SubTopicDTO;
import com.example.github_repo_reader.db.sub_topic.model.SubTopic;
import com.example.github_repo_reader.db.sub_topic.service.SubTopicService;
import com.example.github_repo_reader.db.topic.model.Topic;
import com.example.github_repo_reader.db.topic.service.TopicService;
import com.example.github_repo_reader.index.model.TopicWithSubTopic;
import com.example.github_repo_reader.schedular.PeriodicScheduler;
import com.example.github_repo_reader.utility.DataGenerator;
import com.example.github_repo_reader.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class BaseController {

    @Value("${github.access_token:NOT_SET}")
    private String githubToken;

    private final TopicService topicService;
    private final SubTopicService subTopicService;
    private final BlogService blogService;
    private final LastFetchedService lastFetchedService;
    private final MyRepoService myRepoService;
    private final ContributionService contributionService;

    private final PeriodicScheduler periodicScheduler;

    @GetMapping(value = "/read_contributions")
    public ResponseEntity<Map<String,Object>> reaContributions(){

        final List<MyRepo> myRepoList = myRepoService.readSerially();

        final List<MyRepoDTO> myRepoDTOList = new ArrayList<>();

        for(MyRepo repo : myRepoList){
            final List<ContributionDTO> contributionList = contributionService.readSeriallyByRepoName(repo.getName());

            myRepoDTOList.add( new MyRepoDTO(repo.getName(), repo.getDescription(), repo.getUrl(), contributionList) );
        }

        Map<String,Object> responseMap = Utility.createBasicResponse("Read successful", myRepoDTOList, true);
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping(value = "/force_refresh")
    public ResponseEntity<Map<String,Object>> forceRefresh(){
        final Map<String,Object> responseMap;

        final long currentTime = System.currentTimeMillis();

        final LastFetched lastFetched = lastFetchedService.getLastOne();
        if(lastFetched != null){
            final long dif = (currentTime - lastFetched.getTimestamp());
            if( dif < Utility.TWO_FETCH_MIN_INTERVAL ){ // can't make so many call
                final int minAfterCanCall = Utility.findMinAfterCallCanBeMade(dif);
                responseMap = Utility.createBasicResponse("Can be called after "+minAfterCanCall+" minutes", null, false);
                return ResponseEntity.badRequest().body(responseMap);
            }
        }

        // We run in a background thread or synchronously? Let's run synchronously.
        periodicScheduler.updateRepoAndContributors();
        periodicScheduler.updateCodeBase();
        
        responseMap = Utility.createBasicResponse("Fetched function execution completed", null, true);
        return ResponseEntity.ok().body(responseMap);
    }

    @GetMapping(value = "/read_indices")
    public ResponseEntity<Map<String,Object>> readIndices(){

        final List<Topic> topics = topicService.readSerially();
        final List<SubTopic> subTopics = subTopicService.readSerially();

        final Map<Topic, List<SubTopicDTO>> topicSubtopicsMap = new TreeMap<>();
        for(SubTopic subTopic : subTopics){
            topicSubtopicsMap.computeIfAbsent( subTopic.getTopic(), topic -> new ArrayList<>()).add(
                    new SubTopicDTO(subTopic.getSubTopicName())
            );
        }

        final List<TopicWithSubTopic> topicWithSubTopics = new ArrayList<>();
        for(Topic topic : topics){
            TopicWithSubTopic item = new TopicWithSubTopic(topic.getTopicName(), topic.getNoOfSubTopics(), topicSubtopicsMap.getOrDefault(topic, new ArrayList<>()));
            topicWithSubTopics.add(item);
        }

        Map<String,Object> responseMap = Utility.createBasicResponse("Read successful", topicWithSubTopics, true);
        return ResponseEntity.ok().body(responseMap);
    }

    @GetMapping(value = "/read_blog")
    public ResponseEntity<Map<String,Object>> readBlog(
            @RequestParam (name = "topic_name") String topicName,
            @RequestParam(name = "sub_topic_name") String subTopicName ){

        topicName = DataGenerator.getInstance().getTopicNameFromFolder(topicName);
        final Map<String,Object> responseMap;

        final BlogPK blogPK = new BlogPK(topicName, subTopicName);

        final Blog blog = blogService.findById(blogPK);
        if(blog == null){
            responseMap = Utility.createBasicResponse("Blog not found", null, true);
            return ResponseEntity.badRequest().body(responseMap);
        }

        final BlogDTO blogDTO = new BlogDTO(topicName, subTopicName, blog.getContent());
        responseMap = Utility.createBasicResponse("Read successful", blogDTO, true);
        return ResponseEntity.ok(responseMap);
    }

}
