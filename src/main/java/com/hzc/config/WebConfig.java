package com.hzc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author : hzc
 * @date: 2022/4/12 - 04 - 12 - 16:02
 * @Description: com.hzc.config
 * @version: 1.0
 */
/*
 * @Description: webMVC的配置类
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserArgumentResolver userArgumentResolver;
    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    /*
     * @Description: 加入自定义的参数注入类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    /*
     * @Description: 静态放行
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //写了mvc配置类之后所有的默认的配置都会没有用,包括静态配置
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    /*
     * @Description: 加入自定义的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
    }
}
