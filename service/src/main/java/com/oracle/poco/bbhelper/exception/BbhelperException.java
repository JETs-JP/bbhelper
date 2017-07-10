package com.oracle.poco.bbhelper.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public abstract class BbhelperException extends Exception {

    private static MessageSourceAccessor messageSourceAccessor;

    /*
     * For DI container.
     */
    BbhelperException() {
    }

    public BbhelperException(Throwable cause) {
        this.initCause(cause);
    }

    @Autowired
    private void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        BbhelperException.messageSourceAccessor = messageSourceAccessor;
    }

    public abstract HttpStatus getStatus();

    abstract String getDefaultMessage();

    // TODO getLocaLizedMessageでもいいかもしれないので挙動を確認する
    @Override
    public final String getMessage() {
        return messageSourceAccessor.getMessage(getMessageSourceResolvable());
    }

    public final String getMessage(Locale locale) {
        return messageSourceAccessor.getMessage(getMessageSourceResolvable(), locale);
    }

    private MessageSourceResolvable getMessageSourceResolvable() {
        return new DefaultMessageSourceResolvable(
                new String[]{getClass().getName()}, getDefaultMessage());
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
