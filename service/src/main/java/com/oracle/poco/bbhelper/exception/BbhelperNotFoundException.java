package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BbhelperNotFoundException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Not Found.";

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    BbhelperNotFoundException() {
        super();
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
    private static final long serialVersionUID = -1944294276097426353L;

}
