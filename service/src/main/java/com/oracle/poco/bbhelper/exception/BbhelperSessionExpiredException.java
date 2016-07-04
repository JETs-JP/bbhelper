package com.oracle.poco.bbhelper.exception;

public class BbhelperSessionExpiredException extends BbhelperException {

    public BbhelperSessionExpiredException(
            ErrorDescription description, Throwable cause) {
        super(description, cause);
    }

    public BbhelperSessionExpiredException(
            ErrorDescription description) {
        super(description);
        // TODO Auto-generated constructor stub
    }

}
