package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.log.*;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.Beehive4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.Console;

@SpringBootApplication
public class Application {

    private final static BbhelperLogger logger = BbhelperLogger.getLogger(Application.class);

    @Autowired
    private ApplicationProperties properties;

    private static ApplicationProperties s_properties;
    
    private static boolean force = false;

    @PostConstruct
    public void init() {
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
                logger.info("shutdown.");
            }
        });
        logger.info("started.");
    }

    private static void readOptions(String[] args) {
        for (String arg : args) {
            if ("-f".equals(arg)) {
                force = true;
            }
        }
    }

    private static void checkConnection() {
        logger.info("Checking beehive connection...");
        Console console = System.console();
        String id = console.readLine("ID (email): ");
        String password = new String(console.readPassword("Password: "));
        BeehiveContext context = null;
        try {
            context = BeehiveContext.getBeehiveContext(
                    s_properties.getBeehiveUrl(), id, password);
        } catch (Beehive4jException e) {
            logger.error(new ErrorMessage(
                    Operation.CHECK_CONNECTION, "Connection check failed.", e));
            System.exit(1);
        } finally {
            if (context == null) {
                logger.error(new BasicMessage(
                        Operation.CHECK_CONNECTION, Result.FAIL, "unexpected error."));
                System.exit(1);
            }
        }
    }

}