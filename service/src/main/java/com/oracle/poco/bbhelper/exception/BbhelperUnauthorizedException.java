package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unautorized.")
public class BbhelperUnauthorizedException extends BbhelperException {

    public BbhelperUnauthorizedException(
            ErrorDescription description, Throwable cause) {
        super(description, cause);
    }

    public BbhelperUnauthorizedException(ErrorDescription description) {
        super(description);
        // TODO Auto-generated constructor stub
    }

}
