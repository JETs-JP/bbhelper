package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperUnauthorizedException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private BbhelperLogger logger;

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String session_id =
                request.getHeader(Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION);
        if (session_id == null || session_id.length() == 0) {
            throwUnauthorizedException(request);
        }
        TimeoutManagedContext context = SessionPool.getInstance().get(session_id);
        if (context == null) {
            throwUnauthorizedException(request);
        }
        request.setAttribute(Constants.REQUEST_ATTR_KEY_BEEHIVE_CONTEXT, 
                SessionPool.getInstance().get(session_id));
        return true;
    }

    private void throwUnauthorizedException(HttpServletRequest request)
            throws BbhelperException {
        BbhelperException e = new BbhelperUnauthorizedException(
                ErrorDescription.UNAUTORIZED);
        logger.logBbhelperException(request, e);
        throw e;
    }

}
