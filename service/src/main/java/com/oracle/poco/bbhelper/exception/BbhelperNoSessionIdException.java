package com.oracle.poco.bbhelper.exception;

/**
 * Created by hhayakaw on 2017/07/10.
 */
public final class BbhelperNoSessionIdException extends BbhelperUnauthorizedException {

    private static final String DEFAULT_MESSAGE = "No session id.";

    public BbhelperNoSessionIdException() {
        super();
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -8310868596689915775L;

}
