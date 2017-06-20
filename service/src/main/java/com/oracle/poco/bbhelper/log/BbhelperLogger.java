package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

/**
 * Created by hhayakaw on 2017/06/13.
 */
// TODO スタックトレースの出力
// TODO 内包するメッセージのJSON化。Action,Result,message,cause,detail
// TODO レスポンスを返すときに所要時間を記録
public class BbhelperLogger {

    private final Logger logger;

    private ObjectMapper objectMapper;

    private BbhelperLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.objectMapper = new ObjectMapper();
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

    public void info(BbhelperLogMessage message) {
        try {
            this.logger.info(message.getMarker(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            this.logger.info(message.toString());
        }
    }

    public void warn(String msg) {
        this.logger.warn(msg);
    }

    public void fatal(String msg) {
        this.logger.error(msg);
    }

    public void fatal(String msg, Throwable t) {
        this.logger.error(msg, t);
    }

}
