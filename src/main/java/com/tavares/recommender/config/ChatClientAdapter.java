package com.tavares.recommender.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatClientAdapter {

    private final ChatClient chatClient;

    @Autowired
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
