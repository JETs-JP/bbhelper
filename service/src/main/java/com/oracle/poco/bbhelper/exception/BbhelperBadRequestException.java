package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request.")
public class BbhelperBadRequestException extends BbhelperException {

    public BbhelperBadRequestException(
            ErrorDescription description, Throwable cause, HttpStatus status) {
        super(description, cause, status);
    }

    public BbhelperBadRequestException(ErrorDescription description, HttpStatus status) {
        super(description, status);
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -6506483242163916907L;

}
