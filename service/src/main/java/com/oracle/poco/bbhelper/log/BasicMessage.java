package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 最も基本的なログメッセージ
 *
 * Created by hhayakaw on 2017/06/15.
 */
public class BasicMessage extends BbhelperLogMessageBase {

    // TODO use enum
    private final String action;
    private final String message;
    private final String result;
    private final String cause;

    public static class Builder {

        private String action;
        private String message;
        private String result;
        private String cause;

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder result(String result) {
            this.result = result;
            return this;
        }

        public Builder cause(String cause) {
            this.cause = cause;
            return this;
        }

        public BasicMessage build() {
            if (action == null || action.length() == 0
                    || message == null || message.length() == 0
                    || result == null || result.length() == 0) {
                throw new NullPointerException("Illegal log message.");
            }
            return new BasicMessage(action, message, result, cause);
        }

    }

    BasicMessage() {
        this.action = null;
        this.message = null;
        this.result = null;
        this.cause = null;
    }

    @JsonCreator
    private BasicMessage(@JsonProperty("action") String action, @JsonProperty("message") String message,
                         @JsonProperty("result") String result, @JsonProperty("cause") String cause) {
        super();
        this.action = action;
        this.message = message;
        this.result = result;
        this.cause = cause;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getAction() {
        return action;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getMessage() {
        return message;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getResult() {
        return result;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getCause() {
        return cause;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>(1);
        map.put("action", this.action);
        map.put("message", this.message);
        map.put("result", this.result);
        map.put("cause", this.cause);
        return map;
    }

    @Override
    public String toString() {
        return "BasicMessage{" +
                "action='" + action + '\'' +
                ", message='" + message + '\'' +
                ", result='" + result + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }

}
