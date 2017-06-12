package com.oracle.poco.bbhelper.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized.")
public class BbhelperUnauthorizedException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Unauthorized.";

    private final MessageSourceResolvable messageSourceResolvable;

    public BbhelperUnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
        messageSourceResolvable = new DefaultMessageSourceResolvable(
                new String[]{getClass().getName()}, DEFAULT_MESSAGE);
    }

    public BbhelperUnauthorizedException(Throwable cause) {
        super(cause, HttpStatus.UNAUTHORIZED);
        messageSourceResolvable = new DefaultMessageSourceResolvable(
                new String[]{getClass().getName()}, new Object[]{cause.getLocalizedMessage()},
                DEFAULT_MESSAGE);
    }

    @Override
    public String getCode() {
        return getClass().getName();
    }

    @Override
    public MessageSourceResolvable getMessageSourceResolvable() {
        return messageSourceResolvable;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 9122523362727570400L;

}
