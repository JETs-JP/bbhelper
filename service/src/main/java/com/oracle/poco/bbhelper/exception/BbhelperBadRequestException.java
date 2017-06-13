package com.oracle.poco.bbhelper.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;


@Component
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request.")
public class BbhelperBadRequestException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "Bad Request";

    private final MessageSourceResolvable messageSourceResolvable =
            new DefaultMessageSourceResolvable(new String[]{getClass().getName()}, DEFAULT_MESSAGE);

    public BbhelperBadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public BbhelperBadRequestException(Throwable cause) {
        super(cause, HttpStatus.BAD_REQUEST);
    }

    @Override
    MessageSourceResolvable getMessageSourceResolvable() {
        return messageSourceResolvable;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -6506483242163916907L;

}
