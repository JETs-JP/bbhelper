package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Created by hhayakaw on 2017/06/27.
 */
@Component
public final class BbhelperBeehiveContextExpiredException extends BbhelperUnauthorizedException {

    private static final String DEFAULT_MESSAGE = "Beehive context has expired.";

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    /*
     * For DI container.
     */
    BbhelperBeehiveContextExpiredException() {
        super();
    }

    public BbhelperBeehiveContextExpiredException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getStatus() {
        return STATUS;
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -3115819414184292472L;

}
