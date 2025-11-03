package com.tavares.recommender.music;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MusicServiceCacheTest {

    @Autowired
    private MusicService musicService;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private ChatClientAdapter chatAdapter;

    @BeforeEach
    void setup() {
        Cache cache = cacheManager.getCache("recommendations");
        if (cache != null) cache.clear();
    }

    @Test
    void testCachePreventsDuplicateAIRequests() {
        // Arrange
        String prompt = "happy mood";
        int count = 3;
        String expectedResponse = "1. Album A\n2. Album B\n3. Album C";

        when(chatAdapter.call(anyString(), anyString())).thenReturn(expectedResponse);

        // Act — first call triggers ChatGPT
        String firstResult = musicService.getRecommendations(prompt, count);

        // Act — second call should use cache
        String secondResult = musicService.getRecommendations(prompt, count);

        // Assert — same result
        assertEquals(firstResult, secondResult);

        // Assert — cache contains result
        Cache cache = cacheManager.getCache("musicRecommendations");
        assertNotNull(cache);
        assertNotNull(cache.get(prompt + "_" + count));

        // Verify the AI client was only called once due to caching
        verify(chatAdapter, times(1)).call(anyString(), anyString());
    }
}
