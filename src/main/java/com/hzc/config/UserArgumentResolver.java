package com.hzc.config;

import com.hzc.pojo.TUser;
import com.hzc.service.TUserService;
import com.hzc.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : hzc
 * @date: 2022/4/12 - 04 - 12 - 16:03
 * @Description: com.hzc.config
 * @version: 1.0
 */

/*
 * @Description: 注入参数配置
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    /*
     * @Description: 每次进入接口方法时进行参数类型的判断
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        //如果是User类就返回true并且进行下面的自定义参数注入,如果为false就进行默认参数注入
        return clazz== TUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory){
        //拦截器处理已经注入对象到ThreadLocal中去了
        //已经放到ThreadLocal中去了,直接拿
        return UserContext.getUser();
    }
}
