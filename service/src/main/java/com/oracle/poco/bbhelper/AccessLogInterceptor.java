package com.oracle.poco.bbhelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    // TODO この処理はエラーレスポンスのときにも呼ばれるのかどうか確認
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        logger.response(request);
    }

}
