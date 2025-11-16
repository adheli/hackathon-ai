
package com.tavares.recommender.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tavares.recommender.config.ChatClientAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MusicServiceIntegrationTest {

    @Autowired
    private MusicService musicService;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private ChatClientAdapter chatAdapter;

    @BeforeEach
    void setUp() {
        Cache cache = cacheManager.getCache("musicRecommendations");
        if (cache != null) cache.clear();
    }

    @Test
    void integrationTestCacheAndRecommend() {
        String emotion = "loneliness";
        String expected = "1. Lonely Song - Artist";
        when(chatAdapter.call(anyString(), anyString())).thenReturn(expected);

        String first = musicService.recommend(emotion, MusicType.SONGS);
        String second = musicService.recommend(emotion, MusicType.SONGS);

        assertEquals(expected, first);
        assertEquals(expected, second);
        verify(chatAdapter, times(1)).call(anyString(), anyString());
    }
}
