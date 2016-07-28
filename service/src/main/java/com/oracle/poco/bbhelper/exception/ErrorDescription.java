package com.oracle.poco.bbhelper.exception;

public enum ErrorDescription {
    UNAUTORIZED(1, "unauthorized."),
    BAD_REQUEST(2, "bad request."),
    FAILED_TO_LOG(3, "failed to log."),
    FAILET_TO_LOAD_RESOURCES(4, "failed to load resources data."),
    BEEHIVE4J_CONTEXT_EXPIRED(5, "beehive4j context is expired."),
    BEEHIVE4J_FAULT(6, "some exception raised from beehive4j."),
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
