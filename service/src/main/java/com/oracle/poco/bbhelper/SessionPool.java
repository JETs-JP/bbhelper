package com.oracle.poco.bbhelper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.RandomStringUtils;

class SessionPool {

    private final Map<String, Session> pool =
            new ConcurrentHashMap<String, Session>();

    private static SessionPool instance = null;

    // uninstanciable
    private SessionPool() {}

    static SessionPool getInstance() {
        if (instance == null) {
            instance =  new SessionPool();
        }
        return instance;
    }

    String put(Session context) {
        // TODO same client and user.
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
