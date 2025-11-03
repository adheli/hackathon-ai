package com.tavares.recommender.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MusicService {

    private final ChatClientAdapter chatAdapter;

    @Autowired
    public MusicService(ChatClientAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    @Cacheable(value = "musicRecommendations", key = "#userPrompt + '_' + #count")
    public String getRecommendations(String userPrompt, int count) {
        String systemPrompt = """
            You are a helpful music recommendation assistant.
            Based on the user's description, suggest the requested number of music albums that fit their mood or interest.
            Format the output as a numbered list with album name and artist.
            Be concise but descriptive.
            """;

        String combinedPrompt = String.format(
            "User wants %d music albums. Prompt: %s",
            count, userPrompt
        );

        return chatAdapter.call(systemPrompt, combinedPrompt);
    }
}
