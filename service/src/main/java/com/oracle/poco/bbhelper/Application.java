package com.oracle.poco.bbhelper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.oracle.poco.bbhelper.log.BbhelperLogger;

@SpringBootApplication
public class Application {

    @Autowired
    private BbhelperLogger logger;

    private static BbhelperLogger s_logger;

    @PostConstruct
    public void init() {
        s_logger = logger;
    }

    public static void main(String args[]) {
        ResourceCache.initialize();
        // TODO ここでbeehiveとの接続をチェックしておきたい
        SpringApplication.run(Application.class);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                s_logger.info("shutdown.");
            }
        });
        s_logger.info("started.");
    }

}