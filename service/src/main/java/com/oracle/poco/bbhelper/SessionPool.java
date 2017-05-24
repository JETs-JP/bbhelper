package com.oracle.poco.bbhelper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperUnauthorizedException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
class SessionPool {

    private final Map<String, Session> pool =
            new ConcurrentHashMap<String, Session>();

    // uninstanciable
    private SessionPool() {}

    String put(Session context) {
        refreshPool();
        String session_id;
        do {
            session_id = RandomStringUtils.randomAlphanumeric(32);
        } while (pool.keySet().contains(session_id));
        pool.put(session_id, context);
        return session_id;
    }

    Session use(String session_id) throws BbhelperException {
        Session session = pool.get(session_id);
        if (session == null || !session.isActive()) {
            throw new BbhelperUnauthorizedException(
                    ErrorDescription.UNAUTORIZED, HttpStatus.UNAUTHORIZED);
        }
        session.update();
        return session;
    }

    private void refreshPool() {
        for (Entry<String, Session> entry : pool.entrySet()) {
            if (!entry.getValue().isActive()) {
                pool.remove(entry.getKey());
            }
        }
    }

}
