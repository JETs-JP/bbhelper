package com.oracle.poco.bbhelper.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public abstract class BbhelperException extends Exception {

    private static MessageSourceAccessor messageSourceAccessor;

    private final List<String> messageParameters = new ArrayList<>();

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

    void addMessageParameter(String parameter) {
        this.messageParameters.add(parameter);
    }

    // TODO getLocaLizedMessageでもいいかもしれないので挙動を確認する
    @Override
    public final String getMessage() {
        return messageSourceAccessor.getMessage(getMessageSourceResolvable());
    }

    public final String getMessage(Locale locale) {
        return messageSourceAccessor.getMessage(getMessageSourceResolvable(), locale);
    }

    private MessageSourceResolvable getMessageSourceResolvable() {
        if (messageParameters.isEmpty()) {
            return new DefaultMessageSourceResolvable(
                    new String[]{getClass().getName()}, getDefaultMessage());
        }
        return new DefaultMessageSourceResolvable(
                new String[]{getClass().getName() + "." + messageParameters.size()},
                messageParameters.toArray(),
                getDefaultMessage());
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1431418706349610800L;

}
