package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by hhayakaw on 2017/06/20.
 */
public enum Result {
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    ;

    private final String stringValue;

    Result(String stringValue) {
        this.stringValue = stringValue;
    }

    @JsonValue
    public String getStringValue() {
        return this.stringValue;
    }

    @JsonCreator
    public static Result fromStringValue(String stringValue) {
        for (Result result : Result.values()) {
            if (result.getStringValue().equals(stringValue)) {
                return result;
            }
        }
        return null;
    }

}
