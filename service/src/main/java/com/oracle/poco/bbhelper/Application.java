package com.oracle.poco.bbhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.oracle.poco.bbhelper.log.BbhelperLogger;

@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        BbhelperLogger.initialize();
        ResourceCache.initialize();
        // TODO ここでbeehiveとの接続をチェックしておきたい
        SpringApplication.run(Application.class);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                BbhelperLogger.getInstance().info("shutdown.");
            }
        });
        BbhelperLogger.getInstance().info("started.");
    }

}