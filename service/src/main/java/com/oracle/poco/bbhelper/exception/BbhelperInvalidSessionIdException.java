package com.oracle.poco.bbhelper.exception;

/**
 * Created by hhayakaw on 2017/07/10.
 */
public final class BbhelperInvalidSessionIdException extends BbhelperUnauthorizedException {

    private static final String DEFAULT_MESSAGE = "The Session has been expired or didn't exist.";

    public BbhelperInvalidSessionIdException() {
        super();
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 2650042414871144802L;

}
