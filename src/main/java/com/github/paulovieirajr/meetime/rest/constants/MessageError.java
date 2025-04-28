package com.github.paulovieirajr.meetime.rest.constants;

public enum MessageError {
    INVALID_BEARER_TOKEN("Invalid Authorization header"),
    SESSION_NOT_FOUND("Session not found"),
    TOKEN_EXPIRED("Token expired");

    private final String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
