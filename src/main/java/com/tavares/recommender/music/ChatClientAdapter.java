package com.tavares.recommender.music;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ChatClientAdapter {

    private final ChatClient chatClient;

    public ChatClientAdapter(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String call(String systemPrompt, String combinedPrompt) {
        return chatClient.prompt()
            .system(systemPrompt)
            .user(combinedPrompt)
            .call()
            .content();
    }
}
