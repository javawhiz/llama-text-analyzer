package org.ase.llama_text_analyzer.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ase.llama_text_analyzer.service.TextAnalysisService;
import org.hibernate.validator.constraints.Length;
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

    @PostMapping("/analyze-sentiment")
    public ResponseEntity<String> analyzeSentiment(@RequestBody @NotEmpty @NotNull @Length(min = 10, max = 2000,
            message = "Please send message length between 10 & 2000 characters") String feedback) {
        log.info("Received feedback for sentiment analysis {}", feedback);
        return ResponseEntity.ok(textAnalysisService.analyzeSentiment(feedback));
    }
}
