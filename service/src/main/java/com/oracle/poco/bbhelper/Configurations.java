package com.oracle.poco.bbhelper;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

@Component
@ConfigurationProperties(prefix = "com.oracle.poco.bbhelper")
class Configurations {

    @Autowired
    private BbhelperLogger logger;

    private URL beehiveUrl;

    private long beehiveSessionTimeout;

    URL getBeehiveUrl() {
        return beehiveUrl;
    }

    // TODO 例外処理を実装する
    public void setBeehiveUrl(String beehiveUrl) throws MalformedURLException {
        try {
            this.beehiveUrl = new URL(beehiveUrl);
        } catch (MalformedURLException e) {
            logger.severe(ErrorDescription.FAILET_TO_LOAD_RESOURCES);
            throw e;
        }
    }

    long getBeehiveSessionTimeout() {
        return beehiveSessionTimeout;
    }

    public void setBeehiveSessionTimeout(long beehiveSessionTimeout) {
        this.beehiveSessionTimeout = beehiveSessionTimeout;
    }

}
