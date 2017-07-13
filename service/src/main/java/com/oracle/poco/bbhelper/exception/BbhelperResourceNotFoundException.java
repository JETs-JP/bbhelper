package com.oracle.poco.bbhelper.exception;

/**
 * Created by hhayakaw on 2017/07/13.
 */
public final class BbhelperResourceNotFoundException extends BbhelperNotFoundException {

    private static final String DEFAULT_MESSAGE = "Resource doesn't exist.";

    public BbhelperResourceNotFoundException() {
        super();
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4045827943751549513L;

}
