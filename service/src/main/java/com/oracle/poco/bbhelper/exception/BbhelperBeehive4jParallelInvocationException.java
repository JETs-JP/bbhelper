package com.oracle.poco.bbhelper.exception;

import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.BeehiveApiFaultException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Component
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
                reason = "Some exception raised from beehive4j in parallel invocations.")
public class BbhelperBeehive4jParallelInvocationException extends BbhelperException {

    private static final String DEFAULT_MESSAGE =
            "Some exception raised from beehive4j in parallel invocations.";

    private final List<BbhelperBeehive4jException> causes;

    private final MessageSourceResolvable messageSourceResolvable =
            new DefaultMessageSourceResolvable(new String[]{getClass().getName()}, DEFAULT_MESSAGE);

    public BbhelperBeehive4jParallelInvocationException(List<BbhelperBeehive4jException> causes) {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
        this.causes = causes;
    }

    @Override
    MessageSourceResolvable getMessageSourceResolvable() {
        return messageSourceResolvable;
    }

    public List<BbhelperBeehive4jException> getCauses() {
        return causes;
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 3581390842130243376L;

}
