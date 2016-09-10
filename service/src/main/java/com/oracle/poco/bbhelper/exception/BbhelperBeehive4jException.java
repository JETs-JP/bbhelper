package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.Beehive4jException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
                reason = "some exception raised from beehive4j.")
public class BbhelperBeehive4jException extends BbhelperException {

    public BbhelperBeehive4jException(ErrorDescription description,
            Beehive4jException cause, HttpStatus status) {
        super(description, cause, status);
    }

    public BbhelperBeehive4jException(
            ErrorDescription description, HttpStatus status) {
        super(description, status);
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1823962369781981234L;

}
