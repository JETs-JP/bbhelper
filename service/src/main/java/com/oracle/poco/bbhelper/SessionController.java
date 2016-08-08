package com.oracle.poco.bbhelper;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.exception.BbhelperBadRequestException;
import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.Beehive4jException;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private Configurations config;

    @Autowired
    private BbhelperLogger logger;

    @RequestMapping(path = "/login",
                    method = RequestMethod.GET)
    public ResponseEntity<String> login(HttpServletRequest request)
            throws BbhelperException {
        final String basicAuthHeader = request.getHeader("Authorization");
        try {
            if (basicAuthHeader == null || basicAuthHeader.length() == 0) {
                BbhelperException be = new BbhelperBadRequestException(
                        ErrorDescription.HEADER_FOR_AUTHENTICATION_IS_NOT_SET);
                throw be;
            }

            URL host = new URL(config.getBeehiveUrl());
            BeehiveContext context = 
                    BeehiveContext.getBeehiveContext(host, basicAuthHeader);
            String session_id = SessionPool.getInstance().put(
                    new TimeoutManagedContext(context));
            HttpHeaders headers = new HttpHeaders();
            headers.add(
                    Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        } catch (MalformedURLException e) {
            // TODO URLのチェックは起動時に済ませておきたい.
            return null;
        } catch (Beehive4jException e) {
            BbhelperBeehive4jException be = new BbhelperBeehive4jException(
                    ErrorDescription.BEEHIVE4J_FAULT, e);
            logger.logBbhelperException(request, be);
            throw be;
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
        logger.info("/ping.");
        return "I'm working...";
    }

}
