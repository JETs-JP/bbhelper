package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BbhelperBeehive4jParallelInvocationException extends BbhelperException {

    private static final String DEFAULT_MESSAGE =
            "Some exception raised from beehive4j in parallel invocations.";

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private final List<BbhelperBeehive4jException> causes;

    /*
     * For DI container.
     */
    BbhelperBeehive4jParallelInvocationException() {
        super();
        this.causes = null;
    }

    public BbhelperBeehive4jParallelInvocationException(List<BbhelperBeehive4jException> causes) {
        super();
        this.causes = causes;
    }

    @Override
    public HttpStatus getStatus() {
        return STATUS;
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    public List<BbhelperBeehive4jException> getCauses() {
        return causes;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 3581390842130243376L;

}
