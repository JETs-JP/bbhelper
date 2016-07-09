package com.oracle.poco.bbhelper.exception;

public enum ErrorDescription {
    UNAUTORIZED(1, "unauthorized."),
    BEEHIVE4J_CONTEXT_EXPIRED(2, "beehive4j context is expired."),
    BEEHIVE4J_FAULT(3, "some exception raised from beehive4j."),
    ;

    private static final String CODE_HEADER = "BBHELPER-ERROR-";

    private final String code;
    private final String message;

    ErrorDescription(int code, String message) {
        this.code = CODE_HEADER + String.format("%04d", code);
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFullDescription() {
        return code + ": " + message;
    }

}
