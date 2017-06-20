package com.oracle.poco.bbhelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oracle.poco.bbhelper.log.BasicMessage;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.log.Operation;
import com.oracle.poco.bbhelper.log.Result;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * 本アプリケーションのセッションを、セッションIDと紐付けて格納するためのプール
 */
@Component
class SessionPool {

    private final static BbhelperLogger logger = BbhelperLogger.getLogger(SessionPool.class);

    private final Map<String, Session> pool = new ConcurrentHashMap<>();

    // uninstanciable
    private SessionPool() {}

    /**
     * 新規発行したセッションをプールに保存し、新しいセッションIDを返却します。
     *
     * @param session 新規発行されたセッション
     * @return セッションID
     */
    String put(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session object is not given.");
        }
        String session_id;
        do {
            session_id = RandomStringUtils.randomAlphanumeric(32);
        } while (pool.keySet().contains(session_id));
        pool.put(session_id, session);
        flushInactiveSessions();
        return session_id;
    }

    /**
     * 指定したセッションIDに対応するセッションオブジェクトを返却します。
     * 目的のセッションオブジェクトが存在しないか、タイムアウトしている場合nullを返却します。
     *
     * @param session_id セッションID
     * @return 指定したセッションIDに対応するセッションオブジェクト。
     *         目的のセッションオブジェクトが存在しないか、タイムアウトしている場合nullを返却
     */
    Session use(String session_id) {
        flushInactiveSessions();
        Session session = pool.get(session_id);
        if (session == null) {
            return null;
        }
        session.update();
        return session;
    }

    /**
     * タイムアウトしたセッションオブジェクトを破棄します。
     */
    private void flushInactiveSessions() {
        boolean flushedAny = false;
        for (Map.Entry<String, Session> entry : pool.entrySet()) {
            if (!entry.getValue().isActive()) {
                pool.remove(entry.getKey());
                flushedAny = true;
            }
        }
        if (flushedAny) {
            logger.info(new BasicMessage(Operation.FLUSH_INACTIVE_SESSIONS, Result.SUCCESS,
                    "Inactive sessions are flushed."));
        }
    }

}
