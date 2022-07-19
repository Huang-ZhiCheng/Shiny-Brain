package com.hzc.controller;

import com.hzc.mapper.TUserMapper;
import com.hzc.pojo.TUser;
import com.hzc.service.TUserService;
import com.hzc.utils.MD5Util;
import com.hzc.utils.ValidatorUtil;
import com.hzc.vo.LoginVo;
import com.hzc.vo.RespBean;
import com.hzc.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author : hzc
 * @date: 2022/4/10 - 04 - 10 - 16:14
 * @Description: com.hzc.controller
 * @version: 1.0
 */
/*
 * @Description:登录controller
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private TUserService tUserService;
    /*
     * @Description:跳转登录页面
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
    /*
     * @Description:进行登录
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        return tUserService.doLogin(loginVo,request,response);
    }
}
