package com.pragma.ms_users.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    DOCUMENT_TYPE_NOT_FOUND("The document type does not exist"),
    ADULT("The user must be an adult");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }
}
