package com.oracle.poco.bbhelper.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public abstract class BbhelperException extends Exception {

    private static MessageSourceAccessor messageSourceAccessor;

    private final HttpStatus status;

    public BbhelperException(HttpStatus status) {
        this.status = status;
    }

    public BbhelperException(Throwable cause, HttpStatus status) {
        this.initCause(cause);
        this.status = status;
    }

    @Autowired
    private void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        BbhelperException.messageSourceAccessor = messageSourceAccessor;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return getClass().getName();
    }

    abstract MessageSourceResolvable getMessageSourceResolvable();

    // TODO getLocaLizedMessageでもいいかもしれないので挙動を確認する
    @Override
    public String getMessage() {
        return messageSourceAccessor.getMessage(getMessageSourceResolvable());
    }

    public String getMessage(Locale locale) {
        return messageSourceAccessor.getMessage(getMessageSourceResolvable(), locale);
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
