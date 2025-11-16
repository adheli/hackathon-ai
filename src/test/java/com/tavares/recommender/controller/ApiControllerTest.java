package com.tavares.recommender.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavares.recommender.dtos.MoodRequest;
import com.tavares.recommender.dtos.MusicRecommendationRequest;
import com.tavares.recommender.service.MoodAnalysisService;
import com.tavares.recommender.service.MusicService;
import com.tavares.recommender.service.MusicType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MoodAnalysisService moodAnalysisService;

    @MockitoBean
    private MusicService musicService;

    @Test
    void testAnalyzeEndpoint_ReturnsEmotions() throws Exception {

        // Arrange
        String moodText = "I feel sad and lonely.";
        MoodRequest request = new MoodRequest(moodText);

        Mockito.when(moodAnalysisService.extractEmotions(moodText))
            .thenReturn((List.of("sadness", "loneliness")));

        // Act & Assert
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.emotions[0]").value("sadness"))
            .andExpect(jsonPath("$.emotions[1]").value("loneliness"));
    }

    @Test
    void testRecommendEndpoint_ReturnsList() throws Exception {
        var songs = new String(new ClassPathResource("json/api-controller-music-result.json")
            .getInputStream().readAllBytes());

        // Arrange
        var request = new MusicRecommendationRequest("loneliness", "songs");

        var type = MusicType.getMediaType(request.getMediaType());
        Mockito.when(musicService.recommend(request.getEmotion(), type))
            .thenReturn(songs);

        // Act & Assert
        mockMvc.perform(post("/api/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items[0].title").value("Song A"))
            .andExpect(jsonPath("$.items[1].title").value("Song B"))
            .andExpect(jsonPath("$.items[2].title").value("Song C"));
    }
}
