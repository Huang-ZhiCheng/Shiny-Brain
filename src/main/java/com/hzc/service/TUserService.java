package com.hzc.service;

import com.hzc.pojo.TUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzc.vo.LoginVo;
import com.hzc.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzc
 * @since 2022-04-10
 */
public interface TUserService extends IService<TUser> {
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
    /*
     * @Description: 通过key值去redis中拿user对象信息
     * @param userTicket: 保存在redis中的key值去
     * @return: com.hzc.pojo.TUser
     */
    TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    /*
     * @Description: 更新密码
     * @param userTicket:
     * @param password:
     * @param request:
     * @param response:
     * @return: com.hzc.vo.RespBean
     */
    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}
