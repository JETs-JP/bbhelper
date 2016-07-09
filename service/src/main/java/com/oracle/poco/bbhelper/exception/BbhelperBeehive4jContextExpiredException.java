package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED,
                reason = "beehive4j context is expired.")
public class BbhelperBeehive4jContextExpiredException extends BbhelperException {

    public BbhelperBeehive4jContextExpiredException(
            ErrorDescription description, Throwable cause) {
        super(description, cause);
    }

    public BbhelperBeehive4jContextExpiredException(ErrorDescription description) {
        super(description);
        // TODO Auto-generated constructor stub
    }

}
