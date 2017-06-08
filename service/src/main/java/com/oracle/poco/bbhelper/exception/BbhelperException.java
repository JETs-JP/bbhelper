package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;

public abstract class BbhelperException extends Exception {

    private final String chainedMessage;

    private final HttpStatus status;

    private final String code;

    public BbhelperException(ErrorDescription description, HttpStatus status) {
        super(description.getMessage());
        this.chainedMessage = description.getMessage();
        this.status = status;
        this.code = description.getCode();
    }

    public BbhelperException(
            ErrorDescription description, Throwable cause, HttpStatus status) {
        super(description.getMessage(), cause);
        if (cause == null) {
            this.chainedMessage = description.getMessage();
        } else {
            StringBuilder builder =
                    new StringBuilder(description.getMessage());
            builder.append("(cause: ");
            builder.append(cause.getMessage());
            builder.append(")");
            this.chainedMessage = builder.toString();
        }
        this.status = status;
        this.code = description.getCode();
    }

    @Override
    public String getMessage() {
        return chainedMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
