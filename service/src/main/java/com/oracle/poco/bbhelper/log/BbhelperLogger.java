package com.oracle.poco.bbhelper.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hhayakaw on 2017/06/13.
 */
public class BbhelperLogger {

    private final Logger logger;

    private BbhelperLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public static BbhelperLogger getLogger(Class<?> clazz) {
        return new BbhelperLogger(clazz);
    }

    public void request() {
        this.logger.info("request");
    }

    public void response() {
        this.logger.info("response");
    }

    public void info(String msg) {
        this.logger.info(msg);
    }

    public void fatal(String msg) {
        this.logger.error(msg);
    }

    public void fatal(String msg, Throwable t) {
        this.logger.error(msg, t);
    }
}
