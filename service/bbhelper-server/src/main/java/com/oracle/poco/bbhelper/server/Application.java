package com.oracle.poco.bbhelper.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        BookableResourceCache.initialize();
        SpringApplication.run(Application.class);
    }

}