package com.oracle.poco.bbhelper.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by hhayakaw on 2017/06/27.
 */
@Component
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Beehive context has expired.")
public class BbhelperBeehiveContextExpiredException extends BbhelperBeehive4jException {

    private static final String DEFAULT_MESSAGE = "Beehive context has expired.";

    private final MessageSourceResolvable messageSourceResolvable =
            new DefaultMessageSourceResolvable(new String[]{getClass().getName()}, DEFAULT_MESSAGE);

    BbhelperBeehiveContextExpiredException() {
        super();
    }

    public BbhelperBeehiveContextExpiredException(Throwable cause) {
        super(cause);
    }

    MessageSourceResolvable getMessageSourceResolvable() {
        return messageSourceResolvable;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -3115819414184292472L;

}
