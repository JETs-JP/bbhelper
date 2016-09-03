package com.oracle.poco.bbhelper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.RandomStringUtils;
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

    Session get(String session_id) {
        refreshPool();
        return pool.get(session_id);
    }

    private void refreshPool() {
        for (Entry<String, Session> entry : pool.entrySet()) {
            if (!entry.getValue().isActive()) {
                pool.remove(entry.getKey());
            }
        }
    }

}
