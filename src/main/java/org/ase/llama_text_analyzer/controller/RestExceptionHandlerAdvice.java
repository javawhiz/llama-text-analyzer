package org.ase.llama_text_analyzer.controller;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;
import java.util.Collection;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage());
        problemDetail.setTitle("Validation Error");
        List<String> errors = ex.getAllValidationResults().stream()
                                .map(ParameterValidationResult::getResolvableErrors)
                                .flatMap(Collection::stream)
                                .map(MessageSourceResolvable::getDefaultMessage)
                                .toList();
        problemDetail.setDetail(errors.toString());

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler({
            ConnectException.class
    })
    public ResponseEntity<ProblemDetail> handleConnectException(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
        problemDetail.setTitle("Connection to the external service failed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @ExceptionHandler({
            RuntimeException.class
    })
    public ResponseEntity<ProblemDetail> handleRuntimeException(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
        problemDetail.setTitle("Runtime exception Failure. Please contact the support team");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
