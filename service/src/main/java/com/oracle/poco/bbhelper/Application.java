package com.oracle.poco.bbhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.oracle.poco.bbhelper.log.BbhelperLogger;

@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        BbhelperLogger.initialize();
        ResourceCache.initialize();
        SpringApplication.run(Application.class);
        // TODO 起動、停止ログ
    }

}