package com.oracle.poco.bbhelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.utilities.LoggerManager;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.BeeClientException;

@RestController
@RequestMapping("/session")
public class SessionController {

    private static final String HEADER_KEY_BBH_AUTHORIZED_SESSION =
            "BBH-Authorized-Session";

    private Map<String, BeehiveContext> sessionPool =
            new HashMap<String, BeehiveContext>();

    @RequestMapping(path = "/login",
                    method = RequestMethod.GET)
    public ResponseEntity<String> login(
            @RequestHeader("Authorization") String basicAuthHeader) {
        try {
            URL host = new URL("https://stbeehive.oracle.com/");
            BeehiveContext context = 
                    BeehiveContext.getBeehiveContext(host, basicAuthHeader);
            String session_id = RandomStringUtils.randomAlphanumeric(32);
            sessionPool.put(session_id, context);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        } catch (MalformedURLException | BeeClientException e) {
            LoggerManager.getLogger().severe("EXCEPTION: " + e.getMessage());
            Throwable t = e.getCause();
            if (t != null) {
                LoggerManager.getLogger().severe("CAUSE: " + t.getMessage());
            }
            return new ResponseEntity<String>(
                    null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
