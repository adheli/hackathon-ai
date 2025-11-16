package com.tavares.recommender.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavares.recommender.dtos.MoodRequest;
import com.tavares.recommender.dtos.MoodResponse;
import com.tavares.recommender.dtos.MusicRecommendation;
import com.tavares.recommender.dtos.MusicRecommendationRequest;
import com.tavares.recommender.dtos.MusicRecommendationResponse;
import com.tavares.recommender.service.MoodAnalysisService;
import com.tavares.recommender.service.MusicService;
import com.tavares.recommender.service.MusicType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final MoodAnalysisService moodService;
    private final MusicService musicService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ApiController(MoodAnalysisService moodService, MusicService musicService) {
        this.moodService = moodService;
        this.musicService = musicService;
    }

    @PostMapping(value = "/analyze", consumes = "application/json", produces = "application/json")
    public MoodResponse analyze(@RequestBody MoodRequest request) {
        List<String> emotions = moodService.extractEmotions(request.getMoodText());
        return new MoodResponse(emotions);
    }

    @PostMapping(value = "/recommend", consumes = "application/json", produces = "application/json")
    public MusicRecommendationResponse recommend(@RequestBody MusicRecommendationRequest request) {
        MusicType type = MusicType.getMediaType(request.getMediaType());
        String results = musicService.recommend(request.getEmotion(), type);
        List<MusicRecommendation> items;

        try {
            items = objectMapper.readValue(results, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            items = new ArrayList<>();
        }

        return new MusicRecommendationResponse(items);
    }
}
