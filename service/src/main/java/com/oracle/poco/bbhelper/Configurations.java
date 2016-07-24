package com.oracle.poco.bbhelper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.oracle.poco.bbhelper")
class Configurations {

    private String beehiveUrl;

    String getBeehiveUrl() {
        return beehiveUrl;
    }

    public void setBeehiveUrl(String beehiveUrl) {
        this.beehiveUrl = beehiveUrl;
    }

}
