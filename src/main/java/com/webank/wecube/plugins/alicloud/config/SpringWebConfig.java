package com.webank.wecube.plugins.alicloud.config;

import com.webank.wecube.plugins.alicloud.interceptor.HttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author howechen
 */
@Configuration
public class SpringWebConfig implements WebMvcConfigurer {

    private HttpRequestInterceptor httpRequestInterceptor;

    @Autowired
    public SpringWebConfig(HttpRequestInterceptor httpRequestInterceptor) {
        this.httpRequestInterceptor = httpRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpRequestInterceptor);
    }
}
