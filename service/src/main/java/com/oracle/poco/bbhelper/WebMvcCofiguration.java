package com.oracle.poco.bbhelper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
class WebMvcCofiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAccessLogInterceptor());
        registry.addInterceptor(getSecurityInterceptor())
                .excludePathPatterns("/session/**");
    }

    @Bean
    AccessLogInterceptor getAccessLogInterceptor() {
        return new AccessLogInterceptor();
    }

    @Bean
    SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

}
