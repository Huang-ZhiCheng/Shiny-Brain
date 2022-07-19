package com.hzc.service.impl;

import com.hzc.config.UserContext;
import com.hzc.exception.GlobalException;
import com.hzc.pojo.TUser;
import com.hzc.mapper.TUserMapper;
import com.hzc.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzc.utils.CookieUtil;
import com.hzc.utils.MD5Util;
import com.hzc.utils.UUIDUtil;
import com.hzc.vo.LoginVo;
import com.hzc.vo.RespBean;
import com.hzc.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzc
 * @since 2022-04-10
 */
/*
 * @Description:用户Service类
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService{
    @Autowired
    private TUserMapper tUserMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        TUser tUser = tUserMapper.selectById(mobile);
        if(tUser==null){
            //用户名是否存在
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!MD5Util.formPassToDBPass(password,tUser.getSlat()).equals(tUser.getPassword())){
            //判断密码是否正确
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String ticket = UUIDUtil.uuid();
        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket,tUser);//不在session域中设置而在redis中设置
        //此时也只是redis中有了uuid-tUser的用户信息,但是controller获取用户信息的时候总是通过session拿而不是redis拿
        CookieUtil.setCookie(request,response,"userTicket",ticket);//cookie返回回去
        return RespBean.success(ticket);
    }
    /*
     * @Description:拿到cookie去redis中找该用户
     */
    @Override
    public TUser getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        TUser tUser = (TUser)redisTemplate.opsForValue().get("user:"+userTicket);
        if(tUser!=null){
            //Cookie不为空则更新cookie,刷新持续时间
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return tUser;
    }
    /*
     * @Description:修改密码
     */
    @Override
    public RespBean updatePassword(String userTicket, String password,HttpServletRequest request,HttpServletResponse response) {
        TUser user = UserContext.getUser();
        if(user==null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSlat()));
        int result = tUserMapper.updateById(user);
        if(result==1){
            //删除redis
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }else {
            return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
        }
    }
}
