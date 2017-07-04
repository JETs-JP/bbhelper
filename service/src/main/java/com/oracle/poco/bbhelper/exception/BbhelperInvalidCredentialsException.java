package com.oracle.poco.bbhelper.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Incorrect username or password.")
public class BbhelperInvalidCredentialsException extends BbhelperBeehive4jException {

    private static final String DEFAULT_MESSAGE = "Incorrect username or password.";

    private final MessageSourceResolvable messageSourceResolvable =
            new DefaultMessageSourceResolvable(new String[]{getClass().getName()}, DEFAULT_MESSAGE);

    BbhelperInvalidCredentialsException() {
        super();
    }

    public BbhelperInvalidCredentialsException(Throwable cause) {
        super(cause);
    }

    @Override
    MessageSourceResolvable getMessageSourceResolvable() {
        return messageSourceResolvable;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 206006858163397844L;

}
