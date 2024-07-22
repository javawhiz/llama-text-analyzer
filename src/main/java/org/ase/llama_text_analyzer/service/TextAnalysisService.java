package org.ase.llama_text_analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TextAnalysisService {


    private final OllamaChatModel ollamaChatModel;

    private static final String PROMPT = """
            Act as an English language expert. Please perform sentiment analysis and then summarize this feedback.
            Please do not generate any explanation of the sentiment analysis or the summarization.
                        
            Please Generate the output in below format
            sentiment: {Sentiment of the feedback}
            summary: {Summary of the feedback}
                        
            Here is the Feedback: %s
            """;

    public String analyzeSentiment(String feedback) {
        log.info("Analyzing sentiment for feedback {}", feedback);
        Prompt prompt = new Prompt(String.format(PROMPT, feedback));
        ChatResponse chatResponse = ollamaChatModel.call(prompt);
        List<Generation> results = chatResponse.getResults();
        String response = results.stream().map(generation -> generation.getOutput().getContent())
                                 .collect(Collectors.joining(" "));
        log.info("Response from sentiment analysis {}", response);
        return response;
    }
}
