package com.oracle.poco.bbhelper;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

@Component
@ConfigurationProperties(prefix = "com.oracle.poco.bbhelper")
class ApplicationProperties {

    @Autowired
    private BbhelperLogger logger;

    private URL beehiveUrl;

    private long sessionTimeout;

    URL getBeehiveUrl() {
        return beehiveUrl;
    }

    public void setBeehiveUrl(String beehiveUrl) throws MalformedURLException {
        try {
            this.beehiveUrl = new URL(beehiveUrl);
        } catch (MalformedURLException e) {
            logger.severe(ErrorDescription.FAILED_TO_LOAD_PROPERTIES);
            throw e;
        }
    }

    long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

}
