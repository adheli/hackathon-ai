package com.tavares.recommender.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MusicService {

    private static final String PROMPT_FILENAME = "musicPrompt.json";
    private final ChatClientAdapter chatAdapter;
    private final String prompt;
    private final ResourceLoader resourceLoader;

    @Autowired
    public MusicService(ChatClientAdapter chatAdapter, ResourceLoader resourceLoader) throws IOException {
        this.chatAdapter = chatAdapter;
        this.resourceLoader = resourceLoader;
        this.prompt = readPromptFile();
    }

    @Cacheable(value = "musicRecommendations", key = "#userPrompt + '_' + #count")
    public String getRecommendations(String userPrompt, int count) {
        String combinedPrompt = String.format(
            "User wants %d music albums. Prompt: %s",
            count, userPrompt
        );

        return chatAdapter.call(prompt, combinedPrompt);
    }

    protected String readPromptFile() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + MusicService.PROMPT_FILENAME);
        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());  // include line breaks if needed
            }
            return sb.toString();
        }
    }
}
