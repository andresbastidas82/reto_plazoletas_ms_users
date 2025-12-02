package com.pragma.ms_users.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragma.ms_users.infrastructure.exception.AdultException;
import com.pragma.ms_users.infrastructure.exception.DocumentTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String ERRORS = "errors";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));
        errors.put(ERRORS, errorMessages);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, List<String>>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        String message = "Invalid format or value in the JSON";
        if (ex.getCause() instanceof InvalidFormatException) {
            message = "The birthDate field must be in the format yyyy-MM-dd and be a valid date.";
        }
        Map<String, List<String>> error = new HashMap<>();
        error.put(ERRORS, List.of(message));
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DocumentTypeNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleDocumentTypeNotFoundException(
            DocumentTypeNotFoundException documentTypeNotFoundException) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(ERRORS, List.of(ExceptionResponse.DOCUMENT_TYPE_NOT_FOUND.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(AdultException.class)
    public ResponseEntity<Map<String, List<String>>> handleAdultException(
            AdultException adultException) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(ERRORS, List.of(ExceptionResponse.ADULT.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
