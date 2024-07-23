package org.ase.llama_text_analyzer.service;

import org.ase.llama_text_analyzer.model.TextRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TextAnalysisServiceImplTest {

    @Mock
    private OllamaChatModel ollamaChatModel;

    @Mock
    private ChatClient ollamaChatClient;

    private TextAnalysisServiceImpl textAnalysisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        textAnalysisService = new TextAnalysisServiceImpl(ollamaChatModel, ollamaChatClient);
    }

    @Test
    void analyzeSentimentReturnsExpectedResponse() {
        TextRequest textRequest = new TextRequest();
        textRequest.setText("This is a test.");

        Generation generation = new Generation("positive");
        ChatResponse chatResponse = new ChatResponse(List.of(generation));

        when(ollamaChatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        String response = textAnalysisService.analyzeSentiment(textRequest);

        assertEquals("positive", response);
    }

    @Mock
    ChatClient.ChatClientRequest.CallResponseSpec callResponseSpec;
    @Mock
    ChatClient.ChatClientRequest chatClientRequest;

    @Test
    void sentimentAnalysisViaChatClientReturnsExpectedResponse() {
        TextRequest textRequest = new TextRequest();
        textRequest.setText("This is a test.");

        when(ollamaChatClient.prompt()).thenReturn(chatClientRequest);
        when(chatClientRequest.user(any(String.class))).thenReturn(chatClientRequest);
        when(chatClientRequest.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn("negative");

        String response = textAnalysisService.sentimentAnalysisViaChatClient(textRequest);

        assertEquals("negative", response);
    }
}