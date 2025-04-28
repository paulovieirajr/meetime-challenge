package com.github.paulovieirajr.meetime.rest.exception.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import jakarta.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
// @JsonTypeIdResolver(CaixaBaixaClassResolverConfig.class)
@JsonTypeName("apiErrorResponse")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class ApiErrorResponse {
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("errorCode")
    private Integer errorCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("subErrors")
    private List<ApiSubErrorResponse> subErrors = new ArrayList<>();

    private ApiErrorResponse() {
        timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(HttpStatus status) {
        this();
        this.status = status;
        this.errorCode = status.value();
    }

    public ApiErrorResponse(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.errorCode = status.value();
        this.message = "Unexpected error";
    }

    public ApiErrorResponse(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.errorCode = status.value();
        this.message = message;
    }

    private void addSubError(ApiSubErrorResponse subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationErrorResponse(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationErrorResponse(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    public void addValidationError(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApiSubErrorResponse> getSubErrors() {
        return subErrors;
    }
}
