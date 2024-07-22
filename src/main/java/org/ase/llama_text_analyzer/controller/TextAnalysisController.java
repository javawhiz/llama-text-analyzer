package org.ase.llama_text_analyzer.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/text-analysis")
public class TextAnalysisController {

    @Valid
    @PostMapping("/analyze-sentiment")
    public String analyzeSentiment(@RequestBody @NotEmpty @NotNull @Length(min = 10, max = 500,
            message = "Please send message length between 10 & 100 characters") String feedback) {
        log.info("Received feedback for sentiment analysis {}", feedback);
        return "positive";
    }
}
