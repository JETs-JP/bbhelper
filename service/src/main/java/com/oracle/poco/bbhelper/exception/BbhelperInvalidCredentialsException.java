package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BbhelperInvalidCredentialsException extends BbhelperBeehive4jException {

    private static final String DEFAULT_MESSAGE = "Incorrect username or password.";

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    /*
     * For DI container.
     */
    BbhelperInvalidCredentialsException() {
        super();
    }

    public BbhelperInvalidCredentialsException(Throwable cause) {
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
    private static final long serialVersionUID = 206006858163397844L;

}
