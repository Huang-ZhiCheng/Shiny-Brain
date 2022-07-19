package com.hzc.controller;


import com.hzc.pojo.TUser;
import com.hzc.rabbitmq.MQSender;
import com.hzc.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hzc
 * @since 2022-04-10
 */

/*
 * @Description:用户controller
 */
@Controller
@RequestMapping("/user")
public class TUserController {
    /*
     * @Description: 用户信息
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(TUser user){
        return RespBean.success(user);
    }
}

