package com.oracle.poco.bbhelper.exception;

public enum ErrorDescription {
    UNAUTORIZED(1, "unauthorized."),
    HEADER_FOR_AUTHENTICATION_IS_NOT_SET(2, "header for authentication is not set."),
    FROM_DATE_IS_LATER_THAN_TODATE(3, "fromdate is later than the todate."),
    INVITATION_OUT_OF_RANGE(4, "the invitation to set is out of specified range."),
    INVITATION_OUT_OF_FLOOR_CATEGORY(
            5, "the invitation to set doesn't belong in the sepcified floor category."),
    FAILED_TO_LOG(6, "failed to log."),
    FAILED_TO_LOAD_RESOURCE(7, "failed to load resources data."),
    FAILED_TO_LOAD_PROPERTIES(8, "failed to load app properties."),
    FAILED_TO_CHECK_CONNECTION_WITH_BEEHIVE(9, "failed to check conntection with beehive."),
    BEEHIVE4J_CONTEXT_EXPIRED(10, "beehive4j context is expired."),
    BEEHIVE4J_FAULT(11, "some exception raised from beehive4j."),
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

    public String getFullDescription() {
        return errorCode + ": " + getMessage();
    }

}
