package com.oracle.poco.bbhelper;

import java.io.Console;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.oracle.poco.bbhelper.log.BbhelperLogger;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.Beehive4jException;

@SpringBootApplication
public class Application {

    @Autowired
    private BbhelperLogger logger;

    private static BbhelperLogger s_logger;

    @Autowired
    private ApplicationProperties properties;

    private static ApplicationProperties s_properties;
    
    private static boolean force = false;

    @PostConstruct
    public void init() {
        s_logger = logger;
        s_properties = properties;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        readOptions(args);
        if (!force) {
            checkConnection();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                s_logger.info("shutdown.");
            }
        });
        s_logger.info("started.");
    }

    private static void readOptions(String[] args) {
        for (String arg : args) {
            if ("-f".equals(arg)) {
                force = true;
            }
        }
    }

    private static void checkConnection() {
        System.out.println("Check connection with beehive...");
        Console console = System.console();
        String id = console.readLine("ID (email): ");
        String password = new String(console.readPassword("Password: "));
        BeehiveContext context = null;
        try {
            context = BeehiveContext.getBeehiveContext(
                    s_properties.getBeehiveUrl(), id, password);
        } catch (Beehive4jException e) {
            // TODO Auto-generated catch block
            System.exit(1);
        } finally {
            if (context == null) {
                // TODO log
                System.exit(1);
            }
        }
    }

}