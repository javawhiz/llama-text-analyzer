package org.ase.llama_text_analyzer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.ase.llama_text_analyzer.controller.TextAnalysisController;
import org.ase.llama_text_analyzer.model.TextRequest;
import org.ase.llama_text_analyzer.service.TextAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TextAnalysisControllerTests {

    private MockMvc mockMvc;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        TextAnalysisService textAnalysisService = mock(TextAnalysisService.class);
        when(textAnalysisService.analyzeSentiment(any(TextRequest.class))).thenReturn("positive");

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new TextAnalysisController(textAnalysisService))
                .setValidator(localValidatorFactoryBean)
                .build();
    }

    @Test
    void analyzeSentimentReturnsPositiveForValidRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/text-analysis/analyze-sentiment")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{\"text\":\"This is a test.\"}"))
               .andExpect(status().isOk());
    }

    @Test
    void analyzeSentimentReturnsBadRequestForInvalidRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/text-analysis/analyze-sentiment")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{\"text\":\"\"}")) // Empty text
               .andExpect(status().isBadRequest());
    }

    @Test
    void textRequestValidationFailsForNullText() {
        TextRequest textRequest = new TextRequest();
        textRequest.setText(null); // Null text

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void textRequestValidationFailsForEmptyText() {
        TextRequest textRequest = new TextRequest();
        textRequest.setText(""); // Empty text

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void textRequestValidationFailsForTextExceedingMaxLength() {
        TextRequest textRequest = new TextRequest();
        textRequest.setText("A".repeat(2001)); // Text length exceeds maximum limit

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void textRequestValidationPassesForValidText() {
        TextRequest textRequest = new TextRequest();
        textRequest.setText("This is a test."); // Valid text

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertTrue(violations.isEmpty());
    }
}