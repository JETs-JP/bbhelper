package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 最も基本的なログメッセージ
 * <p>
 * Created by hhayakaw on 2017/06/15.
 */
public class BasicMessage extends BbhelperLogMessageBase {

    private final Operation operation;
    private final Result result;
    private final String message;

    /*
     * for BbhelperJsonLayout
     */
    BasicMessage() {
        this.operation = null;
        this.result = null;
        this.message = null;
    }

    @JsonCreator
    public BasicMessage(@JsonProperty("operation") Operation operation,
                        @JsonProperty("result") Result result,
                        @JsonProperty("message") String message) {
        super();
        this.operation = operation;
        this.result = result;
        this.message = message;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Operation getOperation() {
        return operation;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Result getResult() {
        return result;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        if (this.operation != null) {
            map.put("operation", this.operation.getStringValue());
        }
        if (this.result != null) {
            map.put("result", this.result.getStringValue());
        }
        if (this.message != null) {
            map.put("message", this.message);
        }
        return map;
    }

}
