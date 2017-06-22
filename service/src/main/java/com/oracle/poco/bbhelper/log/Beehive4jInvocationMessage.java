package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by hhayakaw on 2017/06/21.
 */
public class Beehive4jInvocationMessage extends BasicMessage {

    private Long elapsedTime;

    /*
     * for BbhelperJsonLayout
     */
    Beehive4jInvocationMessage() {
        super();
        elapsedTime = null;
    }

    @JsonCreator
    public Beehive4jInvocationMessage(
            @JsonProperty("result") Result result,
            @JsonProperty("message") String message,
            @JsonProperty("elapsedTime") Long elapsedTime) {
        super(Operation.INVOKE_BEEHIVE4J, result, message);
        this.elapsedTime = elapsedTime;
    }

    @JsonInclude
    public Long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        if (elapsedTime != null) {
            map.put("elapsedTime", elapsedTime.toString());
        }
        return map;
    }

}
