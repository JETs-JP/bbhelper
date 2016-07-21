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

import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

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
            // TODO 起動時に読み込むコンフィグレーションとしてまとめておく
            URL host = new URL("https://stbeehive.oracle.com/");
            BeehiveContext context = 
                    BeehiveContext.getBeehiveContext(host, basicAuthHeader);
            String session_id = SessionPool.getInstance().put(
                    new TimeoutManagedContext(context));
            HttpHeaders headers = new HttpHeaders();
            headers.add(
                    SessionPool.HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        } catch (MalformedURLException e) {
            // do nothing.
            return null;
        } catch (Beehive4jException e) {
            BbhelperBeehive4jException be = new BbhelperBeehive4jException(
                    ErrorDescription.BEEHIVE4J_FAULT, e);
            BbhelperLogger.getInstance().logBbhelperException(be);
            return new ResponseEntity<String>(
                    null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * シンプルな文字列を返却します。サーバーの稼働を確認する目的で設けられたAPIです。
     * 
     * @return 文字列 "I'm working..."
     */
    @RequestMapping(value = "/ping",
                    method = RequestMethod.GET)
    public String ping() {
        return "I'm working...";
    }

}
