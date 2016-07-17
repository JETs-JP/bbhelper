package com.oracle.poco.bbhelper.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * このアプリケーションのために構成されたロガーを提供するためのユーティリティ・
 * クラスです。
 *
 * このアプリケーションで実装されるログ出力は、すべて本クラスを使用することを
 * 想定しています。
 *
 * @author hhayakaw
 *
 */
public class BbhelperLogger {

    /**
     * ロガーのプロパティファイルのパス
     */
    private static final String FILE_PATH_LOGGING_PROPERTIES =
            "logging.properties";
    /**
     * ロガーの名前。このアプリケーションでは、この名前意外のロガーは持たない
     */
    private static final String LOGGER_NAME =
            "com.oracle.bbhelper.logger";
    /**
     * ロガー本体はコンポジションで保持
     */
    private Logger logger;
    /**
     * 
     */
    private static BbhelperLogger instance = null;

    // uninstanciable
    private BbhelperLogger() {
        initInternal();
    }

    private void initInternal() {
        final InputStream in = this.getClass().getClassLoader().
                getResourceAsStream(FILE_PATH_LOGGING_PROPERTIES);
        if (in == null) {
            throw new IllegalStateException("Failed to load logging.properties.");
        }
        try {
            LogManager.getLogManager().readConfiguration(in);
            logger = Logger.getLogger(LOGGER_NAME);
            logger.addHandler(new FileHandler());
            logger.addHandler(new AccessLogFileHandler());
        } catch (SecurityException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * ロガーを取得します。
     *
     * @return ロガー
     */
    public static BbhelperLogger getInstance() {
        if (instance == null) {
            instance = new BbhelperLogger();
        }
        return instance;
    }

    /**
     * @param message
     */
    public void info(String message) {
        if (message == null || message.length() == 0) {
            // do nothing.
            return;
        }
        logger.info(message);
    }

    /**
     * @param message
     */
    public void severe(String message) {
        if (message == null || message.length() == 0) {
            // do nothing.
            return;
        }
        logger.severe(message);
    }

    /**
     * @param t
     */
    public void logThrowable(Throwable t) {
        if (t == null) {
            // do nothing.
            return;
        }
        // TODO メッセージをパラメータで受け取る
        logger.log(Level.SEVERE, "ERROR", t);
        // デバッグログは出力先を分ける
        logger.warning(getStrackTraceString(t));
    }

    private String getStrackTraceString(Throwable t) {
        StackTraceElement[] elements = t.getStackTrace();
        StringBuffer buf = new StringBuffer();
        Arrays.stream(elements).forEach(e -> {
            buf.append(e.toString());
            buf.append("\n");
            });
        return buf.toString();
    }

}
