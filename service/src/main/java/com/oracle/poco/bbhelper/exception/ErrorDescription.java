package com.oracle.poco.bbhelper.exception;

public enum ErrorDescription {
    UNAUTORIZED(1, "unauthorized."),
    BAD_REQUEST(2, "bad request."),
    INVITATION_OUT_OF_RANGE(3, "the invitation to set is out of specified range."),
    FAILED_TO_LOG(4, "failed to log."),
    FAILET_TO_LOAD_RESOURCES(5, "failed to load resources data."),
    BEEHIVE4J_CONTEXT_EXPIRED(6, "beehive4j context is expired."),
    BEEHIVE4J_FAULT(7, "some exception raised from beehive4j."),
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
