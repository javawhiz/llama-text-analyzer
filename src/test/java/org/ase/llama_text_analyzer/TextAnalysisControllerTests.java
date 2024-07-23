package org.ase.llama_text_analyzer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.ase.llama_text_analyzer.model.TextRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TextAnalysisControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
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