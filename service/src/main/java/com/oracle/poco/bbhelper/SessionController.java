package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;

import com.oracle.poco.bbhelper.log.BasicMessage;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.log.Operation;
import com.oracle.poco.bbhelper.log.Result;
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

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private static final BbhelperLogger logger = BbhelperLogger.getLogger(SessionController.class);

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
            this.logger.info(new BasicMessage(Operation.LOGIN, Result.FAIL, "no credential."));
            throw new BbhelperBadRequestException();
        }
        String session_id;
        try {
            BeehiveContext context =
                    BeehiveContext.getBeehiveContext(properties.getBeehiveUrl(), basicAuthHeader);
            Session session = new Session(context);
            factory.autowireBean(session);
            session_id = sessionPool.put(session);
            this.logger.info(new BasicMessage(Operation.LOGIN, Result.SUCCESS, "login"));
        } catch (BeehiveApiFaultException e) {
            // TODO エラーの原因をログに残す
            this.logger.info(new BasicMessage(Operation.LOGIN, Result.FAIL, "login failed."));
            // TODO 失敗の原因に合わせた例外を上げる
            throw new BbhelperBeehive4jException(e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

}