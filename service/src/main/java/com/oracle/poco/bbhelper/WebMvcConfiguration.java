package com.oracle.poco.bbhelper;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAccessLogInterceptor());
        registry.addInterceptor(getSecurityInterceptor())
                .excludePathPatterns("/api/session/**")
                .excludePathPatterns("/api/system/echo");
    }

    @Bean
    AccessLogInterceptor getAccessLogInterceptor() {
        return new AccessLogInterceptor();
    }

    @Bean
    SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Bean
    FilterRegistrationBean mdcInsertingFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new MDCInsertingServletFilter());
        bean.setOrder(1);
        return bean;
    }

}
