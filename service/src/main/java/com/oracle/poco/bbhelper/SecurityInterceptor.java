package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperUnauthorizedException;

class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionPool sessionPool;

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String session_id =
                request.getHeader(Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION);
        if (session_id == null || session_id.length() == 0) {
            BbhelperException e = new BbhelperUnauthorizedException();
            throw e;
        }
        Session session = sessionPool.use(session_id);
        if (session == null) {
            BbhelperException e = new BbhelperUnauthorizedException();
            throw e;
        }
        request.setAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION, session);
        return true;
    }

}
