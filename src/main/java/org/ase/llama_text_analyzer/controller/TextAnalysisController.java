package org.ase.llama_text_analyzer.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ase.llama_text_analyzer.model.TextRequest;
import org.ase.llama_text_analyzer.service.TextAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/text-analysis")
public class TextAnalysisController {

    private final TextAnalysisService textAnalysisService;

    @PostMapping("/analyze-sentiment-via-chat-client")
    public ResponseEntity<String> analyzeSentimenViaChatzClient(@Valid @RequestBody @NotNull TextRequest textRequest) {
        log.info("Received feedback for sentiment analysis {}", textRequest.getText());
        return ResponseEntity.ok(textAnalysisService.sentimentAnalysisViaChatClient(textRequest));
    }

    @PostMapping("/analyze-sentiment")
    public ResponseEntity<String> analyzeSentiment(@Valid @RequestBody @NotNull TextRequest textRequest) {
        log.info("Received feedback for sentiment analysis {}", textRequest.getText());
        return ResponseEntity.ok(textAnalysisService.analyzeSentiment(textRequest));
    }
}
