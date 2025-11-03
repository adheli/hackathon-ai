package com.tavares.recommender.music;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MusicController.class)
class MusicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MusicService musicService;

    @Test
    void testIndexPageLoads() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    void testRecommendReturnsExpectedViewAndModel() throws Exception {
        Mockito.when(musicService.getRecommendations(anyString(), anyInt()))
            .thenReturn("1. Album X - Artist Y");

        mockMvc.perform(post("/recommend")
                .param("prompt", "happy")
                .param("count", "3"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("prompt", "count", "recommendations"))
            .andExpect(view().name("index"))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Album X")));
    }
}

