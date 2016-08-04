package com.oracle.poco.bbhelper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
                reason = "Internal Server Error.")
public class BbhelperInternalServerErrorException extends BbhelperException {

    public BbhelperInternalServerErrorException(
            ErrorDescription description, Throwable cause) {
        super(description, cause);
    }

    public BbhelperInternalServerErrorException(ErrorDescription description) {
        super(description);
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 6803211962816485700L;

}
