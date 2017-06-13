package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.log.BbhelperLogger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
@ConfigurationProperties(prefix = "com.oracle.poco.bbhelper")
class ApplicationProperties {

    private final BbhelperLogger logger =
            BbhelperLogger.getLogger(ApplicationProperties.class);

    private URL beehiveUrl;

    private long sessionTimeout;

    URL getBeehiveUrl() {
        return beehiveUrl;
    }

    public void setBeehiveUrl(String beehiveUrl) throws MalformedURLException {
        try {
            this.beehiveUrl = new URL(beehiveUrl);
        } catch (MalformedURLException e) {
            logger.fatal("Failed to load application properties.", e);
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
