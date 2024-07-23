package org.ase.llama_text_analyzer.service;

import org.ase.llama_text_analyzer.model.TextRequest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public interface TextAnalysisService {

    @Retryable(maxAttempts = 2, retryFor = {Exception.class}, backoff = @Backoff(delay = 3000, multiplier = 2,
            maxDelay = 10000))
    String analyzeSentiment(TextRequest textRequest);

    @Retryable(maxAttempts = 2, retryFor = {Exception.class}, backoff = @Backoff(delay = 3000, multiplier = 2,
            maxDelay = 10000))
    String sentimentAnalysisViaChatClient(TextRequest textRequest);
}
