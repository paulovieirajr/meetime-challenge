package com.github.paulovieirajr.meetime.rest.exception.response;

public class ApiValidationErrorResponse implements ApiSubErrorResponse {
    private final String object;
    private final String message;
    private String field;
    private Object rejectedValue;

    ApiValidationErrorResponse(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiValidationErrorResponse(String object, String field, Object rejectedValue, String message) {
        this.object = object;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public String getObject() {
        return object;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public String getMessage() {
        return message;
    }
}