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

import jp.gr.java_conf.hhayakawa_jp.beehive4j.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.BeehiveApiFaultException;

// TODO このクラスにロギングを実装する
@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private SessionPool sessionPool;

    @Autowired
    private AutowireCapableBeanFactory factory;

    @RequestMapping(path = "/actions/login",
                    method = RequestMethod.GET)
    public ResponseEntity<String> login(HttpServletRequest request) throws BbhelperException {
        final String basicAuthHeader = request.getHeader("Authorization");
        if (basicAuthHeader == null || basicAuthHeader.length() == 0) {
            throw new BbhelperBadRequestException();
        }
        try {
            BeehiveContext context =
                    BeehiveContext.getBeehiveContext(properties.getBeehiveUrl(), basicAuthHeader);
            Session session = new Session(context);
            factory.autowireBean(session);
            String session_id = sessionPool.put(session);
            HttpHeaders headers = new HttpHeaders();
            headers.add(Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        } catch (BeehiveApiFaultException e) {
            throw new BbhelperBeehive4jException();
        }
    }

}
