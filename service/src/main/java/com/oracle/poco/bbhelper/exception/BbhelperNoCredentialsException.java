package com.oracle.poco.bbhelper.exception;

/**
 * Created by hhayakaw on 2017/07/10.
 */
public final class BbhelperNoCredentialsException extends BbhelperUnauthorizedException {

    private static final String DEFAULT_MESSAGE = "No credentials.";

    public BbhelperNoCredentialsException() {
        super();
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 5841720883069870667L;

}
