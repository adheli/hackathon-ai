
package com.tavares.recommender.service;

import com.tavares.recommender.config.ChatClientAdapter;
import com.tavares.recommender.prompts.PromptLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MusicService {

    private final ChatClientAdapter chatAdapter;
    private static final String SYSTEM_PROMPT_FILE = "music-prompt.json";

    @Autowired
    public MusicService(ChatClientAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    @Cacheable(value = "musicRecommendations", key = "#emotion + '_' + #type")
    public String recommend(String emotion, MusicType type) {

        String systemMessage = loadSystemPrompt();

        String mediaType = type.getString();
        String userPrompt = "Emotion: " + emotion + "\nType: " + mediaType;

        return chatAdapter.call(systemMessage, userPrompt);
    }

    private String loadSystemPrompt() {
        String systemPrompt = """
            You are a helpful music recommendation assistant.
            Based on the user's input, suggest 5 music albums that fit their mood or interest.
            Format the output as a numbered list with album name and artist.
            Be concise but descriptive.
            """;

        var loader = new PromptLoader(SYSTEM_PROMPT_FILE, systemPrompt);

        return loader.loadPromptAsString();
    }
}
