package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oracle.poco.bbhelper.exception.BbhelperInvalidSessionIdException;
import com.oracle.poco.bbhelper.exception.BbhelperNoSessionIdException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.log.ErrorMessage;
import com.oracle.poco.bbhelper.log.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oracle.poco.bbhelper.exception.BbhelperException;

class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionPool sessionPool;

    private static final BbhelperLogger logger = BbhelperLogger.getLogger(SecurityInterceptor.class);
    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String session_id =
                request.getHeader(Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION);
        if (session_id == null || session_id.length() == 0) {
            BbhelperException e = new BbhelperNoSessionIdException();
            logger.info(new ErrorMessage(Operation.CHECK_SESSION_ID, e));
            throw e;
        }
        Session session = sessionPool.use(session_id);
        if (session == null) {
            BbhelperException e = new BbhelperInvalidSessionIdException();
            logger.info(new ErrorMessage(Operation.CHECK_SESSION_ID, e));
            throw e;
        }
        request.setAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION, session);
        return true;
    }

}
