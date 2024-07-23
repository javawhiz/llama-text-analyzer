package org.ase.llama_text_analyzer.service;

import org.ase.llama_text_analyzer.model.TextRequest;

public interface TextAnalysisService {
    String analyzeSentiment(TextRequest textRequest);

    String sentimentAnalysisViaChatClient(TextRequest textRequest);
}
