package com.oracle.poco.bbhelper;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

class ErrorResponse {

    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final List<Detail> details = new ArrayList<>();

    ErrorResponse(int status, String error, String code, String message) {
        super();
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public void addDetail(Detail detail) {
        details.add(detail);
    }

    class Detail {

        private final String message;

        Detail(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

    }

}
