package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.log.BbhelperLogger;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class AccessLogInterceptor extends HandlerInterceptorAdapter {

    private static final BbhelperLogger logger =
            BbhelperLogger.getLogger(AccessLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String requestId = RandomStringUtils.randomAlphanumeric(32);
        MDC.put(Constants.REQUEST_ATTR_KEY_REQUEST_ID, requestId);
        logger.request();
        return true;
    }

    // TODO この処理はエラーレスポンスのときにも呼ばれるのかどうか確認
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        logger.response();
    }

}
