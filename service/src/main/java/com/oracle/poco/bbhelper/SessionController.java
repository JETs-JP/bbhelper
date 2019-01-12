package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperInvalidCredentialsException;
import com.oracle.poco.bbhelper.exception.BbhelperNoCredentialsException;
import com.oracle.poco.bbhelper.log.*;
import jp.gr.java_conf.hhiroshell.beehive4j.BeehiveContext;
import jp.gr.java_conf.hhiroshell.beehive4j.exception.BeehiveApiFaultException;
import jp.gr.java_conf.hhiroshell.beehive4j.exception.BeehiveUnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
            BbhelperException e = new BbhelperNoCredentialsException();
            this.logger.info(new ErrorMessage(Operation.LOGIN, e));
            throw e;
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
            BbhelperException bbhe;
            if (e instanceof BeehiveUnauthorizedException) {
                bbhe = new BbhelperInvalidCredentialsException(e);
                logger.info(new ErrorMessage(Operation.LOGIN, bbhe));
            } else {
                bbhe = new BbhelperBeehive4jException(e);
                logger.error(new ErrorMessage(Operation.LOGIN, bbhe));
            }
            throw bbhe;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION, session_id);
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

}