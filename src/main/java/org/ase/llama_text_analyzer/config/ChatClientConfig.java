package org.ase.llama_text_analyzer.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    private static final String PROMPT = """
            Act as an English language expert. Please perform sentiment analysis and then summarize this feedback.
            Please do not generate any explanation of the sentiment analysis or the summarization and Respond using JSON.
                        
            Please Generate the output in below format
            sentiment: Sentiment of the feedback
            summary: Summary of the feedback
            """;

    @Bean
    public ChatClient ollamaChatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(PROMPT)
                      .build();
    }
}
