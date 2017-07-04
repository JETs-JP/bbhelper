package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Created by hhayakaw on 2017/06/22.
 */
public class ErrorMessage extends BasicMessage {

    private final Error error;

    /*
     * for BbhelperJsonLayout
     */
    ErrorMessage() {
        super();
        error = null;
    }

    /*
     * for BbhelperJsonLayout
     */
    @JsonCreator
    ErrorMessage(@JsonProperty("operation") Operation operation,
                 @JsonProperty("localizedMessage") String message,
                 @JsonProperty("error") Error e) {
        super(operation, Result.FAIL, message);
        this.error = e;
    }

    public ErrorMessage(Operation operation, Throwable cause) {
        super(operation, Result.FAIL, cause.getLocalizedMessage());
        this.error = new Error(cause);
    }

    public ErrorMessage(Operation operation, String message, Throwable cause) {
        super(operation, Result.FAIL, message);
        this.error = new Error(cause);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Error getError() {
        return error;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("error", this.error.toMap());
        return map;
    }

    static class Error {

        private final String localizedMessage;
        private final List<String> stackTrace;
        private final Error cause;

        private Error(Throwable throwable) {
            this.localizedMessage = throwable.getLocalizedMessage();
            this.stackTrace = new LinkedList<String>();
            Arrays.stream(throwable.getStackTrace()).forEach(e -> stackTrace.add(e.toString()));
            if (throwable.getCause() != null) {
                this.cause = new Error(throwable.getCause());
            } else {
                this.cause = null;
            }

        }

        /*
         * for BbhelperJsonLayout
         */
        @JsonCreator
        Error(@JsonProperty("localizedMessage") String localizedMessage,
              @JsonProperty("stackTrace") List<String> stackTrace,
              @JsonProperty("cause") Error cause) {
            this.localizedMessage = localizedMessage;
            this.stackTrace = stackTrace;
            this.cause = cause;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getLocalizedMessage() {
            return localizedMessage;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public List<String> getStackTrace() {
            return stackTrace;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Error getCause() {
            return cause;
        }

        private Map<String, Object> toMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("localizedMessage", localizedMessage);
            map.put("stackTrace", stackTrace);
            if (cause != null) {
                map.put("cause", cause.toMap());
            }
            return map;
        }

    }

}
