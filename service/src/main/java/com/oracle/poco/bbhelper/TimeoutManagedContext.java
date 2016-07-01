package com.oracle.poco.bbhelper;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveInvoker;

public class TimeoutManagedContext {

    public static final long TIMEOUT = 1000 * 60 * 60; //1hour

    private long lastUsed;

    private final BeehiveContext context;

    TimeoutManagedContext(BeehiveContext context) {
        super();
        this.lastUsed = System.currentTimeMillis();
        this.context = context;
    }

    boolean isActive() {
        return (System.currentTimeMillis() - lastUsed) < TIMEOUT;
    }

    void update() {
        lastUsed = System.currentTimeMillis();
    }

    public <T extends BeehiveInvoker<?>> T getInvoker(Class<T> InvokerType) 
        throws BbhelperException {
        if (!isActive()) {
            // throw error crrectly.
            throw new BbhelperException();
        }
        update();
        return context.getInvoker(InvokerType);
    }

}
