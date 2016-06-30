package com.oracle.poco.bbhelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.RandomStringUtils;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveContext;

class SessionPool {

    static final String HEADER_KEY_BBH_AUTHORIZED_SESSION =
            "BBH-Authorized-Session";

    private final Map<String, BeehiveContext> pool =
            new ConcurrentHashMap<String, BeehiveContext>();

    private static SessionPool instance = null;

    // uninstanciable
    private SessionPool() {}

    static SessionPool getInstance() {
        if (instance == null) {
            instance =  new SessionPool();
        }
        return instance;
    }

    boolean isAuthorizedSession(String session_id) {
        refreshPool();
        return pool.keySet().contains(session_id);
    }

    String put(BeehiveContext context) {
        // TODO same client and user.
        refreshPool();
        String session_id;
        do {
            session_id = RandomStringUtils.randomAlphanumeric(32);
        } while (pool.keySet().contains(session_id));
        pool.put(session_id, context);
        return session_id;
    }

    BeehiveContext get(String session_id) {
        refreshPool();
        return pool.get(session_id);
    }

    private void refreshPool() {
        // expire timeouted sessions.
    }

}
