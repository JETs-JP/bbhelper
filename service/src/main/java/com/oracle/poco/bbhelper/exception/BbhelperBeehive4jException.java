package com.oracle.poco.bbhelper.exception;

import jp.gr.java_conf.hhiroshell.beehive4j.exception.Beehive4jException;
import jp.gr.java_conf.hhiroshell.beehive4j.exception.BeehiveHttpClientErrorException;
import jp.gr.java_conf.hhiroshell.beehive4j.exception.BeehiveHttpErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public final class BbhelperBeehive4jException extends BbhelperException {

    private static final String DEFAULT_MESSAGE = "An exception raised from beehive4j.";

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    /*
     * For DI container.
     */
    BbhelperBeehive4jException() {
        super();
    }

    public BbhelperBeehive4jException(Beehive4jException cause) {
        super(cause);
        if (cause instanceof BeehiveHttpErrorException) {
            status = ((BeehiveHttpErrorException) cause).getHttpStatus();
        }
        /*
         * Beehiveからの4xx系のエラーは、クライアントに返してもよいメッセージとみなす
         */
        if (cause instanceof BeehiveHttpClientErrorException) {
            addMessageParameter(((BeehiveHttpClientErrorException) cause).getReason());
        }
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1823962369781981234L;

}
