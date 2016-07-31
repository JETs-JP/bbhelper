package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oracle.poco.bbhelper.log.BbhelperLogger;

class AccessLogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private BbhelperLogger logger;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(Constants.REQUEST_ATTR_KEY_REQUEST_ID,
                RandomStringUtils.randomAlphanumeric(32));
        logger.request(request);
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
        logger.response(request);
    }

}
