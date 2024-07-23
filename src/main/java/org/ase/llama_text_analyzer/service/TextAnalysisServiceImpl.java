package org.ase.llama_text_analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ase.llama_text_analyzer.model.TextRequest;
import org.springframework.ai.chat.client.ChatClient;
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
public class TextAnalysisServiceImpl implements TextAnalysisService {


    private final OllamaChatModel ollamaChatModel;
    private final ChatClient ollamaChatClient;

    private static final String PROMPT = """
            Act as an English language expert. Please perform sentiment analysis and then summarize this feedback.
            Please do not generate any explanation of the sentiment analysis or the summarization and Respond using JSON.
                        
            Please Generate the output in below format
            sentiment: [Sentiment of the feedback]
            summary: [Summary of the feedback]
                        
            Here is the Feedback: %s
            """;

    @Override
    public String analyzeSentiment(TextRequest textRequest) {
        log.info("Analyzing sentiment for feedback {}", textRequest.toString());
        Prompt prompt = new Prompt(String.format(PROMPT, textRequest));
        ChatResponse chatResponse = ollamaChatModel.call(prompt);
        List<Generation> results = chatResponse.getResults();
        String response = results.stream().map(generation -> generation.getOutput().getContent())
                                 .collect(Collectors.joining(" "));
        log.info("Response from sentiment analysis {}", response);
        return response;
    }

    @Override
    public String sentimentAnalysisViaChatClient(TextRequest textRequest) {
        return ollamaChatClient.prompt()
                               .user(textRequest.getText())
                               .call()
                               .content();
    }
}
