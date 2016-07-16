package com.oracle.poco.bbhelper.utilities;

import java.util.Arrays;
import java.util.logging.Level;
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
     * ロガーの名前。このアプリケーションでは、この名前意外のロガーは持たない
     */
    private static final String LOGGER_NAME =
            "com.oracle.bbhelper.server.logger";
    /**
     * デフォルトのログレベル
     */
    private static final Level LOG_LEVEL_DEFAULT = Level.CONFIG;
    /**
     * ログレベルを指定する環境変数のキー名
     */
    private static final String KEY_ENV_LOG_LEVEL = "EMV_LOG_LEVEL";
    /**
     * ロガーはコンポジションで保持
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
        logger = Logger.getLogger(LOGGER_NAME);
        Level level = loadLogLevel();
        logger.setLevel(level);
        logger.log(Level.INFO, "logger is initialized. log level is \"{0}\".",
                level.toString());
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
     * 環境変数に設定されたログレベルを返却します。設定されていないか、無効な値
     * の場合はデフォルト値を返します。
     *
     * 本メソッドは、結果をキャッシュしません。環境変数を変更した後に本メソッド
     * を実行すると、現時点（変更後）の値が取得されます。
     *
     * @return 環境変数に指定されたログレベル
     */
    private Level loadLogLevel() {
        String envValue = System.getenv(KEY_ENV_LOG_LEVEL);
        if (envValue == null || envValue.length() == 0) {
            return LOG_LEVEL_DEFAULT;
        }
        Level level = null;
        try {
            level = Level.parse(envValue);
            if (level == null) {
                return LOG_LEVEL_DEFAULT;
            }
        } catch (IllegalArgumentException e) {
            return LOG_LEVEL_DEFAULT;
        }
        return level;
    }

    /**
     * @param t
     */
    public void logThrowable(Throwable t) {
        if (t == null) {
            // do nothing.
            return;
        }
        logger.severe(t.getMessage());
        // デバッグログは出力先を分ける
        logger.warning(getStrackTraceString(t));
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
