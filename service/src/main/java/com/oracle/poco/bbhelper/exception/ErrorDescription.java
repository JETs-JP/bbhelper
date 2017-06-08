package com.oracle.poco.bbhelper.exception;

public enum ErrorDescription {
    // 400
    FROM_DATE_IS_LATER_THAN_TODATE(0, "fromdate is later than the todate."),
    // 401
    UNAUTHORIZED(1, "unauthorized."),
    HEADER_FOR_AUTHENTICATION_IS_NOT_SET(2, "header for authentication is not set."),
    // 500
    FAILED_TO_LOAD_RESOURCE(3, "failed to load resources data."),
    FAILED_TO_LOAD_PROPERTIES(4, "failed to load app properties."),
    FAILED_TO_CHECK_CONNECTION_WITH_BEEHIVE(5, "failed to check conntection with beehive."),
    BEEHIVE4J_CONTEXT_EXPIRED(6, "beehive4j context is expired."),
    BEEHIVE4J_FAULT(7, "some exception raised from beehive4j."),
    ;

    private static final String ERROR_CODE_HEADER = "BBHELPER-ERROR-";

    private final String errorCode;

    private final String message;

    ErrorDescription(int code, String message) {
        this.errorCode = ERROR_CODE_HEADER + String.format("%04d", code);
        this.message = message;
    }

    public String getCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

}
