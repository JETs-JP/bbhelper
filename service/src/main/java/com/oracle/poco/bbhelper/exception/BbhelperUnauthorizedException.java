package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unautorized.")
public class BbhelperUnauthorizedException extends BbhelperException {

    public BbhelperUnauthorizedException(
            ErrorDescription description, Throwable cause, HttpStatus status) {
        super(description, cause, status);
    }

    public BbhelperUnauthorizedException(
            ErrorDescription description, HttpStatus status) {
        super(description, status);
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 9122523362727570400L;

}
