package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BbhelperBadRequestException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Bad Request.";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    /*
     * For DI container.
     */
    BbhelperBadRequestException() {
        super();
    }

    public BbhelperBadRequestException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getStatus() {
        return STATUS;
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -2193154457674383079L;

}
