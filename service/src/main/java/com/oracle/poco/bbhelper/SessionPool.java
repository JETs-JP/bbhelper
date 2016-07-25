package com.oracle.poco.bbhelper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.RandomStringUtils;

class SessionPool {

    private final Map<String, TimeoutManagedContext> pool =
            new ConcurrentHashMap<String, TimeoutManagedContext>();

    private static SessionPool instance = null;

    // uninstanciable
    private SessionPool() {}

    static SessionPool getInstance() {
        if (instance == null) {
            instance =  new SessionPool();
        }
        return instance;
    }

    String put(TimeoutManagedContext context) {
        // TODO same client and user.
        refreshPool();
        String session_id;
        do {
            session_id = RandomStringUtils.randomAlphanumeric(32);
        } while (pool.keySet().contains(session_id));
        pool.put(session_id, context);
        return session_id;
    }

    TimeoutManagedContext get(String session_id) {
        refreshPool();
        return pool.get(session_id);
    }

    private void refreshPool() {
        for (Entry<String, TimeoutManagedContext> entry : pool.entrySet()) {
            if (!entry.getValue().isActive()) {
                pool.remove(entry.getKey());
            }
        }
    }

}
