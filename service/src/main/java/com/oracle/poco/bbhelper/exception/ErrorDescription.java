package com.oracle.poco.bbhelper.exception;

public enum ErrorDescription {
    UNAUTORIZED(1, "unauthorized."),
    HEADER_FOR_AUTHENTICATION_IS_NOT_SET(2, "header for authentication is not set."),
    FROM_DATE_IS_LATER_THAN_TODATE(3, "fromdate is later than the todate."),
    INVITATION_OUT_OF_RANGE(4, "the invitation to set is out of specified range."),
    FAILED_TO_LOG(5, "failed to log."),
    FAILET_TO_LOAD_RESOURCES(6, "failed to load resources data."),
    BEEHIVE4J_CONTEXT_EXPIRED(7, "beehive4j context is expired."),
    BEEHIVE4J_FAULT(8, "some exception raised from beehive4j."),
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
