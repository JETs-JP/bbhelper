package com.oracle.poco.bbhelper.server;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * このアプリケーションのために構成されたロガーを提供するためのユーティリティ・クラスです
 *
 * このアプリケーションで実装されるログ出力は、すべて本クラスから取得したロガーを使用することを想定しています。
 *
 * @author hiroshi.hayakawa@oracle.com
 *
 */
public class LoggerManager {

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
     * このアプリケーション唯一のロガー
     */
    private static Logger logger;

    /**
     * ロガーの初期化処理
     *
     * 環境変数で指定されたログレベル（指定がなければデフォルトのログレベル）を指定する。ACCSの設計指針
     * として、動的に環境変数を読み込めるようにすることが推奨されている
     *
     * ACCSのログは標準出力／標準エラー出力がそのまま出力されるため、Handlerの指定は不要
     */
    public static void initialize() {
        if (logger != null) {
            return;
        }
        initInternal();
    }

    /**
     * ロガーを取得します。
     * ロガーの初期化が行われていない場合、このタイミングで初期化処理が行われます。
     *
     * @return ロガー
     */
    public static Logger getLogger() {
        if (logger == null) {
            initialize();
        }
        return logger;
    }

    /**
     * ロガーを初期化します。
     * 環境変数に設定されたログレベルがロガーに反映されます。設定されていないか、無効な値の場合はデフォルト
     * 値隣ります。
     *
     * 環境変数を変更した後に本メソッドを実行すると、現時点（変更後）の値になります。
     */
    public static void reInitialize() {
        initInternal();
    }

    private static void initInternal() {
        logger = Logger.getLogger(LOGGER_NAME);
        Level level = loadLogLevel();
        logger.setLevel(level);
        logger.log(Level.INFO, "logger is initialized. log level is \"{0}\".",
                level.toString());
    }

    /**
     * 環境変数に設定されたログレベルを返却します。設定されていないか、無効な値の場合はデフォルト
     * 値を返します。
     *
     * 本メソッドは、結果をキャッシュしません。環境変数を変更した後に本メソッドを実行すると、現時点
     * （変更後）の値が取得されます。
     *
     * @return 環境変数に指定されたログレベル
     */
    private static Level loadLogLevel() {
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

}
