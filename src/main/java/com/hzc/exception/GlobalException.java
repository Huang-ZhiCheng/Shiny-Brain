package com.hzc.exception;

import com.hzc.vo.RespBeanEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author : hzc
 * @date: 2022/4/11 - 04 - 11 - 14:43
 * @Description: com.hzc.exception
 * @version: 1.0
 */
/*
 * @Description:可抛全局异常类
 */
public class GlobalException extends RuntimeException{

    private RespBeanEnum respBeanEnum;

    public GlobalException() {
    }

    public GlobalException(RespBeanEnum loginError) {
        respBeanEnum = loginError;
    }

    public RespBeanEnum getRespBeanEnum() {
        return respBeanEnum;
    }

    public void setRespBeanEnum(RespBeanEnum respBeanEnum) {
        this.respBeanEnum = respBeanEnum;
    }
}
