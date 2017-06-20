package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by hhayakaw on 2017/06/20.
 */
public enum Operation {
    LOGIN("LOGIN"),
    REQUEST("REQUEST"),
    RESPONSE("RESPONSE"),
    ;

    private final String stringValue;

    Operation(String stringValue) {
        this.stringValue = stringValue;
    }

    @JsonValue
    public String getStringValue() {
        return this.stringValue;
    }

    @JsonCreator
    public static Operation fromStringValue(String stringValue) {
        for (Operation operation : Operation.values()) {
            if (operation.getStringValue().equals(stringValue)) {
                return operation;
            }
        }
        return null;
    }

}
