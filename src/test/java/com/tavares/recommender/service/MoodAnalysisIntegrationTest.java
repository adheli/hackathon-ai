
package com.tavares.recommender.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.tavares.recommender.config.ChatClientAdapter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MoodAnalysisIntegrationTest {

    @Autowired
    private MoodAnalysisService moodService;

    @MockitoBean
    private ChatClientAdapter chatAdapter;

    @Test
    void integrationExtractEmotions() {
        when(chatAdapter.call(anyString(), anyString())).thenReturn("loneliness, sadness, emptiness");
        List<String> res = moodService.extractEmotions("I feel empty and alone");
        assertNotNull(res);
        assertFalse(res.isEmpty());
        assertEquals("loneliness", res.getFirst());
    }
}
