package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request.")
public class BbhelperBadRequestException extends BbhelperException {

    public BbhelperBadRequestException(
            ErrorDescription description, Throwable cause) {
        super(description, cause);
    }

    public BbhelperBadRequestException(ErrorDescription description) {
        super(description);
    }

}
