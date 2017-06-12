package com.oracle.poco.bbhelper.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;

public abstract class BbhelperException extends Exception {

    private final Throwable cause;

    private final HttpStatus status;

    public BbhelperException(HttpStatus status) {
        this.cause = null;
        this.status = status;
    }

    public BbhelperException(Throwable cause, HttpStatus status) {
        this.cause = cause;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    abstract public String getCode();

    abstract public MessageSourceResolvable getMessageSourceResolvable();

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
