package com.oracle.poco.bbhelper;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.utilities.BbhelperLogger;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.Beehive4jException;

@RestController
@RequestMapping("/session")
public class SessionController {

    @RequestMapping(path = "/login",
                    method = RequestMethod.GET)
    public ResponseEntity<String> login(
            @RequestHeader("Authorization") String basicAuthHeader) {
        try {
            URL host = new URL("https://stbeehive.oracle.com/");
            BeehiveContext context = 
                    BeehiveContext.getBeehiveContext(host, basicAuthHeader);
            String session_id = SessionPool.getInstance().put(
                    new TimeoutManagedContext(context));
            HttpHeaders headers = new HttpHeaders();
            headers.add(
                    SessionPool.HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        } catch (MalformedURLException | Beehive4jException e) {
            BbhelperLogger.getInstance().log(e);
            Throwable t = e.getCause();
            if (t != null) {
                BbhelperLogger.getInstance().log(t);
            }
            return new ResponseEntity<String>(
                    null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
