package org.ase.llama_text_analyzer.controller;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage());
        problemDetail.setTitle("Validation Error");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        ex.getAllValidationResults().get(0).getResolvableErrors().getFirst().getDefaultMessage();

        List<String> errors = ex.getAllValidationResults().stream()
                                .map(ParameterValidationResult::getResolvableErrors)
                                .flatMap(Collection::stream)
                                .map(MessageSourceResolvable::getDefaultMessage)
                                .toList();
        problemDetail.setDetail(errors.toString());

        return ResponseEntity.badRequest().body(problemDetail);
    }
}
