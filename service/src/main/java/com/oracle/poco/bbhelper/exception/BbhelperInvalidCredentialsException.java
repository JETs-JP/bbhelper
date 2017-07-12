package com.oracle.poco.bbhelper.exception;

import org.springframework.stereotype.Component;

@Component
public final class BbhelperInvalidCredentialsException extends BbhelperUnauthorizedException {

    private static final String DEFAULT_MESSAGE = "Incorrect username or password.";

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
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 206006858163397844L;

}
