package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by hhayakaw on 2017/06/20.
 */
public enum Operation {
    LOAD_PROPERTIES("LOAD_PROPERTIES"),
    LOAD_RESOURCES("LOAD_RESOURCES"),
    CHECK_CONNECTION("CHECK_CONNECTION"),
    VALIDATION("VALIDATION"),
    LOGIN("LOGIN"),
    CHECK_SESSION_ID("CHECK_SESSION_ID"),
    FLUSH_INACTIVE_SESSIONS("FLUSH_INACTIVE_SESSIONS"),
    PULL_RESOURCES_FROM_CACHE("PULL_RESOURCES_FROM_CACHE"),
    INVOKE_BEEHIVE4J("INVOKE_BEEHIVE4J"),
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
