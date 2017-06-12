package com.oracle.poco.bbhelper.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request.")
public class BbhelperBadRequestException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Bad Request";

    private final MessageSourceResolvable messageSourceResolvable;

    public BbhelperBadRequestException() {
        super(HttpStatus.BAD_REQUEST);
        messageSourceResolvable = new DefaultMessageSourceResolvable(
                new String[]{getClass().getName()}, DEFAULT_MESSAGE);
    }

    public BbhelperBadRequestException(Throwable cause) {
        super(cause, HttpStatus.BAD_REQUEST);
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
    private static final long serialVersionUID = -6506483242163916907L;

}
