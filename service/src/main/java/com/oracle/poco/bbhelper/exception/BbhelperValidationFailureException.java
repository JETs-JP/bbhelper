package com.oracle.poco.bbhelper.exception;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

// TODO 複数のエラーがちゃんと記録に残るようにする
@Component
public class BbhelperValidationFailureException extends BbhelperBadRequestException {

    private static final String DEFAULT_MESSAGE = "Invalid request body or parameter(s).";

    private final BindingResult bindingResult;

    /*
     * For DI container.
     */
    BbhelperValidationFailureException() {
        super();
        this.bindingResult = null;
    }

    public BbhelperValidationFailureException(Throwable cause, BindingResult bindingResult) {
        super(cause);
        this.bindingResult = bindingResult;
    }

    @Override
    String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4597298664562696426L;

}
