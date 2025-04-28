package com.github.paulovieirajr.meetime.rest.exception.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BearerTokenInvalidException extends RuntimeException {

    public BearerTokenInvalidException(String message) {
        super(message);
    }
}
