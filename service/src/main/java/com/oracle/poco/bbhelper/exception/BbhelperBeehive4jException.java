package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BbhelperBeehive4jException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Some exception raised from beehive4j.";

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    /*
     * For DI container.
     */
    BbhelperBeehive4jException() {
        super();
    }

    public BbhelperBeehive4jException(Throwable cause) {
        super(cause);
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    @Override
    public HttpStatus getStatus() {
        return STATUS;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1823962369781981234L;

}
