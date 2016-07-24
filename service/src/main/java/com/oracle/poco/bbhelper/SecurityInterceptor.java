package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperUnauthorizedException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        BbhelperLogger.getInstance().debug("SecurityInterceptor#preHandle()");
        String session_id =
                request.getHeader(Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION);
        if (session_id == null || session_id.length() == 0 || 
                !SessionPool.getInstance().isAuthorizedSession(session_id)) {
            BbhelperException e = new BbhelperUnauthorizedException(
                    ErrorDescription.UNAUTORIZED);
            BbhelperLogger.getInstance().logBbhelperException(e);
            throw e;
        }
        return true;
    }

}
