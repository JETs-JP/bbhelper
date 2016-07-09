package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.Beehive4jException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
                reason = "some exception raised from beehive4j.")
public class BbhelperBeehive4jException extends BbhelperException {

    public BbhelperBeehive4jException(
            ErrorDescription description, Beehive4jException cause) {
        super(description, cause);
        // TODO Auto-generated constructor stub
    }

    public BbhelperBeehive4jException(ErrorDescription description) {
        super(description);
        // TODO Auto-generated constructor stub
    }

}
