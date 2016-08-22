package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;

public abstract class BbhelperException extends Exception {

    private final String chainedMessage;

    private final HttpStatus status;

    public BbhelperException(ErrorDescription description, HttpStatus status) {
        super(description.getFullDescription());
        this.chainedMessage = description.getFullDescription();
        this.status = status;
    }

    public BbhelperException(
            ErrorDescription description, Throwable cause, HttpStatus status) {
        super(description.getFullDescription(), cause);
        if (cause == null) {
            this.chainedMessage = description.getFullDescription();
        } else {
            StringBuilder builder =
                    new StringBuilder(description.getFullDescription());
            builder.append("(cause: ");
            builder.append(cause.getMessage());
            builder.append(")");
            this.chainedMessage = builder.toString();
        }
        this.status = status;
    }

    @Override
    public String getMessage() {
        return chainedMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
