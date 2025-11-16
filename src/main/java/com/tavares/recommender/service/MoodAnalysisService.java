
package com.tavares.recommender.service;

import com.tavares.recommender.config.ChatClientAdapter;
import com.tavares.recommender.prompts.PromptLoader;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoodAnalysisService {

    private final ChatClientAdapter chatAdapter;
    private static final String SYSTEM_PROMPT_FILE = "emotion-prompt.json";

    @Autowired
    public MoodAnalysisService(ChatClientAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    public List<String> extractEmotions(String moodDescription) {

        String systemMessage = loadSystemPrompt();

        String response = chatAdapter.call(systemMessage, moodDescription);

        return Arrays.stream(response.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();
    }

    private String loadSystemPrompt() {
        String system = """
            You are an expert emotional analysis assistant.
            Given a user's mood description, return ONLY a comma-separated list
            of emotions they might be feeling. Keep it short, 3 to 6 emotions.
            Do not explain, do not add dialogue.
            """;

        var loader = new PromptLoader(SYSTEM_PROMPT_FILE, system);

        return loader.loadPromptAsString();
    }
}
