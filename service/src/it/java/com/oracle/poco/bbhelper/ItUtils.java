package com.oracle.poco.bbhelper;

import java.util.Base64;

public class ItUtils {

    static String makeBasicAuthString(String user, String password) {
        if (user.contains(":")) {
            throw new IllegalArgumentException(
                    "User name must not contain \":\".");
        }
        String src = user.trim() + ":" + password;
        byte[] encoded = Base64.getEncoder().encode(src.getBytes());
        return "Basic " + new String(encoded);
    }

}
