package com.oracle.poco.bbhelper.agent;

final class BeehiveCredential {

    private final String session;
    private final String antiCsrfToken;

    BeehiveCredential(String session, String antiCsrfToken) {
        super();
        this.session = session;
        this.antiCsrfToken = antiCsrfToken;
    }

    String getSession() {
        return session;
    }

    String getAnticsrf() {
        return antiCsrfToken;
    }

    @Override
    public String toString() {
        return "BeehiveCredential [session=" + session + ", antiCsrfToken=" +
                antiCsrfToken + "]";
    }

}
