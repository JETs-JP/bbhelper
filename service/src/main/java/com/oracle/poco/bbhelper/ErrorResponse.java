package com.oracle.poco.bbhelper;

public class ErrorResponse {

    private final int status;
    private final String error;
    private final String message;

    public ErrorResponse(int status, String error, String message) {
        super();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
