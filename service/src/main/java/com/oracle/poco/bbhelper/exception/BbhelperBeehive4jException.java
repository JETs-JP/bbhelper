package com.oracle.poco.bbhelper.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.Beehive4jException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
                reason = "Some exception raised from beehive4j.")
public class BbhelperBeehive4jException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Some exception raised from beehive4j.";

    private final MessageSourceResolvable messageSourceResolvable;

    public BbhelperBeehive4jException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
        messageSourceResolvable = new DefaultMessageSourceResolvable(
                new String[]{getClass().getName()}, DEFAULT_MESSAGE);
    }

    public BbhelperBeehive4jException(Throwable cause, HttpStatus status) {
        super(cause, HttpStatus.INTERNAL_SERVER_ERROR);
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
    private static final long serialVersionUID = 1823962369781981234L;

}
