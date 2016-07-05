package com.oracle.poco.bbhelper.exception;

public class BbhelperException extends Exception {

    private final ErrorDescription description;

    public BbhelperException(ErrorDescription description) {
        super(description.getFullDescription());
        this.description = description;
    }

    public BbhelperException(
            ErrorDescription description, Throwable cause) {
        super(description.getFullDescription(), cause);
        this.description = description;
    }

    public ErrorDescription getErrorDescription() {
        return description;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
