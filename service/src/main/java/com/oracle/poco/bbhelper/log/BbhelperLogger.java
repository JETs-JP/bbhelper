package com.oracle.poco.bbhelper.log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.oracle.poco.bbhelper.Constants;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;

/**
 * このアプリケーションのために構成されたロガーを提供するためのユーティリティ・
 * クラスです。<br>
 * このアプリケーションで実装されるログ出力は、すべて本クラスを使用することを
 * 想定しています。
 * 
 * 実装の方針:
 *       パラメータ
 *
 * @author hhayakaw
 *
 */
@Component
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

    // uninstanciable
    private BbhelperLogger() {
        initInternal();
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
     * @param request
     */
    public void request(HttpServletRequest request) {
        if (request == null) {
            // このメソッドの誤った使い方
            throw new NullPointerException("request is null.");
        }
        AccessLogger.info("REQUEST: " + (String)request.getAttribute(
                Constants.REQUEST_ATTR_KEY_REQUEST_ID));
    }

    /**
     * @param message
     */
    public void response(HttpServletRequest request) {
        if (request == null) {
            // このメソッドの誤った使い方
            throw new NullPointerException("request is null.");
        }
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
     * @param request
     * @param description
     */
    public void severe(HttpServletRequest request, ErrorDescription description) {
        if (description == null) {
            // do nothing.
            SystemLogger.severe("Required parameter for logging \"ErrorDescription\" is empty.");
            return;
        }
        if (request == null) {
            severe(description);
        }
        String message = getBaseMessage(
                request, description.getFullDescription()).toString();
        SystemLogger.severe(message);
    }

    /**
     * @param description
     */
    public void severe(ErrorDescription description) {
        if (description == null) {
            // do nothing.
            SystemLogger.severe("Required parameter for logging \"ErrorDescription\" is empty.");
            return;
        }
        SystemLogger.severe(description.getFullDescription());
    }

    /**
     * @param reqest
     * @param e
     */
    public void exception(HttpServletRequest request, BbhelperException e) {
        if (e == null) {
            // TODO パラメータ不足の時の処理を共通化
            SystemLogger.severe(
                    "Required parameter for logging \"BbhelperException\" is empty.");
            return;
        }
        String errMessage =  e.getChainedMessage();
        String message = getBaseMessage(request, errMessage).toString();
        SystemLogger.severe(message);
        DebugLogger.log(Level.SEVERE, message, e);
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
        // TODO requestがNullの時のケアが足りてない
        builder.append("REQUEST_ID: ").append(request_id).append(",");
        builder.append("REQUEST_URL: ").append(request.getRequestURL()).append(",");
        builder.append("MESSAGE: ").append(message);
        return builder;
    }

}
