package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
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

import jp.gr.java_conf.hhayakawa_jp.beehive4j.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.BeehiveApiFaultException;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private SessionPool sessionPool;

    @Autowired
    private AutowireCapableBeanFactory factory;

    @RequestMapping(path = "/login",
                    method = RequestMethod.GET)
    public ResponseEntity<String> login(HttpServletRequest request)
            throws BbhelperException {
        final String basicAuthHeader = request.getHeader("Authorization");
        if (basicAuthHeader == null || basicAuthHeader.length() == 0) {
            throw new BbhelperBadRequestException(
                    ErrorDescription.HEADER_FOR_AUTHENTICATION_IS_NOT_SET,
                    HttpStatus.BAD_REQUEST);
        }
        try {
            BeehiveContext context = BeehiveContext.getBeehiveContext(
                    properties.getBeehiveUrl(), basicAuthHeader);
            Session session = new Session(context);
            factory.autowireBean(session);
            String session_id = sessionPool.put(session);
            HttpHeaders headers = new HttpHeaders();
            headers.add(
                    Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        } catch (BeehiveApiFaultException e) {
            throw new BbhelperBeehive4jException(
                    ErrorDescription.BEEHIVE4J_FAULT, e, e.getHttpStatus());
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
