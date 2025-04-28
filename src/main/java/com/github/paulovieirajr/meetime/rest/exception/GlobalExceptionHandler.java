package com.github.paulovieirajr.meetime.rest.exception;

import com.github.paulovieirajr.meetime.rest.exception.common.BearerTokenInvalidException;
import com.github.paulovieirajr.meetime.rest.exception.common.SessionNotFoundException;
import com.github.paulovieirajr.meetime.rest.exception.response.ApiErrorResponse;
import feign.FeignException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        var apiErrorResponse = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        apiErrorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }

    @ExceptionHandler({ FeignException.class, FeignException.FeignClientException.class })
    public ResponseEntity<ApiErrorResponse> handleFeignException(FeignException ex) {
        var status = ex.status();
        var apiErrorResponse = new ApiErrorResponse((HttpStatus) HttpStatusCode.valueOf(status));
        apiErrorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }

    @ExceptionHandler(BearerTokenInvalidException.class)
    public ResponseEntity<ApiErrorResponse> handleTokenException(BearerTokenInvalidException ex) {
        var apiErrorResponse = new ApiErrorResponse(BAD_REQUEST);
        apiErrorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleSessionNotFoundException(SessionNotFoundException ex) {
        var apiErrorResponse = new ApiErrorResponse(HttpStatus.NOT_FOUND);
        apiErrorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        var apiErrorResponse = new ApiErrorResponse(BAD_REQUEST);
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiErrorResponse.addValidationError(ex.getBindingResult().getGlobalErrors());
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }
}
