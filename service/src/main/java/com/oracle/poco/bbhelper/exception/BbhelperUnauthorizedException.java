package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BbhelperUnauthorizedException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Unauthorized.";

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    BbhelperUnauthorizedException() {
        super();
    }

    BbhelperUnauthorizedException(Throwable cause) {
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
    private static final long serialVersionUID = 9122523362727570400L;

}
