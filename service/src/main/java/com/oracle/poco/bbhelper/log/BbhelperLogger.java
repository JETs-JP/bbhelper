package com.oracle.poco.bbhelper.log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.oracle.poco.bbhelper.Constants;
import com.oracle.poco.bbhelper.exception.BbhelperException;

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
     * ロガーの名前: システムログ
     */
    private static final String LOGGER_NAME_SYSTEM =
            "com.oracle.bbhelper.log.system";
    /**
     * ロガーの名前: アクセスログ
     */
    private static final String LOGGER_NAME_ACCESS =
            "com.oracle.bbhelper.log.access";
    /**
     * ロガーの名前: デバッグログ
     */
    private static final String LOGGER_NAME_DEBUG =
            "com.oracle.bbhelper.log.debug";
    /**
     * ロガー本体: システムログ
     */
    private Logger SystemLogger;
    /**
     * ロガー本体: アクセスログ
     */
    private Logger AccessLogger;
    /**
     * ロガー本体: デバッグログ
     */
    private Logger DebugLogger;
    /**
     * このアプリケーション唯一のロガー
     */
    private static BbhelperLogger instance = null;

    // uninstanciable
    private BbhelperLogger() {
        initInternal();
    }

    /**
     * 初期化
     */
    public static void initialize() {
        if (instance == null) {
            instance = new BbhelperLogger();
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

    private void initInternal() {
        File logdir = new File("logs");
        if (!logdir.exists() || !logdir.isDirectory()) {
            if (!logdir.mkdir()) {
                System.out.println("ERROR! : Can't make log directory.");
                System.exit(1);
            }
        }
        final InputStream in = this.getClass().getClassLoader().
                getResourceAsStream(FILE_PATH_LOGGING_PROPERTIES);
        if (in == null) {
            throw new IllegalStateException("Failed to load logging.properties.");
        }
        try {
            LogManager.getLogManager().readConfiguration(in);
            SystemLogger = Logger.getLogger(LOGGER_NAME_SYSTEM);
            SystemLogger.addHandler(new ConsoleHandler());
            SystemLogger.addHandler(new SystemLogFileHandler());
            AccessLogger = Logger.getLogger(LOGGER_NAME_ACCESS);
            AccessLogger.addHandler(new ConsoleHandler());
            AccessLogger.addHandler(new AccessLogFileHandler());
            DebugLogger = Logger.getLogger(LOGGER_NAME_DEBUG);
            DebugLogger.addHandler(new ConsoleHandler());
            DebugLogger.addHandler(new DebugLogFileHandler());
        } catch (SecurityException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param sessionId
     */
    public void request(HttpServletRequest request) {
        if (request == null) {
            // TODO 何らかのログを残してこのメソッドを終了する
            return;
        }
        // TODO requestから欲しい情報を取得して記録しておく
        AccessLogger.info("REQUEST: " + (String)request.getAttribute(
                Constants.REQUEST_ATTR_KEY_REQUEST_ID));
    }

    /**
     * @param message
     */
    public void response(HttpServletRequest request) {
        if (request == null) {
            // TODO 何らかのログを残してこのメソッドを終了する
            return;
        }
        // TODO requestから欲しい情報を取得して記録しておく
        AccessLogger.info("RESPONSE: " + (String)request.getAttribute(
                Constants.REQUEST_ATTR_KEY_REQUEST_ID));
    }

    /**
     * @param message
     */
    public void info(String message) {
        if (message == null || message.length() == 0) {
            // do nothing.
            return;
        }
        SystemLogger.info(message);
    }

    /**
     * @param message
     */
    public void severe(String message) {
        if (message == null || message.length() == 0) {
            // do nothing.
            return;
        }
        SystemLogger.severe(message);
    }

    /**
     * @param reqest
     * @param e
     */
    public void logBbhelperException(
            HttpServletRequest request, BbhelperException e) {
        if (e == null) {
            return;
        }
        logThrowable(request, e.getErrorDescription().getFullDescription(), e);
    }

    /**
     * @param t
     */
    private void logThrowable(
            HttpServletRequest request, String message, Throwable t) {
        if (t == null) {
            // do nothing.
            return;
        }
        String logMessage = getBaseMessage(request, message).toString();
        SystemLogger.severe(logMessage);
        DebugLogger.log(Level.SEVERE, logMessage, t);
    }

    /**
     * @param message
     */
    public void debug(String message) {
        if (message == null || message.length() == 0) {
            // do nothing.
            return;
        }
        DebugLogger.fine(message);
    }

    private StringBuilder getBaseMessage(
            HttpServletRequest request, String message) {
        String request_id;
        if (request == null) {
            request_id = "N/A";
        }
        request_id = (String)request.getAttribute(
                Constants.REQUEST_ATTR_KEY_REQUEST_ID);
        StringBuilder builder = new StringBuilder();
        builder.append("REQUEST_ID: ").append(request_id).append(",");
        builder.append("REQUEST_URL: ").append(request.getRequestURL()).append(",");
        builder.append("MESSAGE: ").append(message);
        return builder;
    }

}
