package com.oracle.poco.bbhelper.exception;

public abstract class BbhelperException extends Exception {

    private final String chainedMessage;

    public BbhelperException(ErrorDescription description) {
        super(description.getFullDescription());
        chainedMessage = description.getFullDescription();
    }

    public BbhelperException(
            ErrorDescription description, Throwable cause) {
        super(description.getFullDescription(), cause);
        if (cause == null) {
            chainedMessage = description.getFullDescription();
        } else {
            chainedMessage = description.getFullDescription() + ": "
                    + cause.getMessage();
        }
    }

    @Override
    public String getMessage() {
        return chainedMessage;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
