package com.hzc.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:  组合拦截器形成通用接口限流。适用于方法,在运行时触发
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    /* 多少秒内 */
    int second();
    /* 最大访问次数 */
    int maxCount();
    /* 是否需要登录 */
    boolean needLogin() default true;
}
