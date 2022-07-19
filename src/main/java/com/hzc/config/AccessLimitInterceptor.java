package com.hzc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzc.pojo.TUser;
import com.hzc.service.TUserService;
import com.hzc.utils.CookieUtil;
import com.hzc.vo.RespBean;
import com.hzc.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author : hzc
 * @date: 2022/4/19 - 04 - 19 - 19:12
 * @Description: com.hzc.config
 * @version: 1.0
 */
/* 请求次数拦截器(实现HandlerInterceptor接口) */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private TUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*
     * @Description: 对需要限流(有限流注解)的方法进行拦截
     * @param handler: 当前方法
     * @return: boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断处理的是不是一个方法
        if(handler instanceof HandlerMethod){
            TUser user = getUser(request,response);
            HandlerMethod hm = (HandlerMethod)handler;
            //放到本地线程里面
            UserContext.setUser(user);
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                //没有该限流注解
                return true;
            }else {
                //有该注解,获取时间段内最大访问次数以及是否需要登录
                int maxCount = accessLimit.maxCount();
                boolean needLogin = accessLimit.needLogin();
                int second = accessLimit.second();
                String key = request.getRequestURI();
                //需要登录
                if(needLogin){
                    //登录信息为空
                    if(user==null){
                        render(response,RespBeanEnum.SESSION_ERROR);
                        return false;
                    }
                    //请求地址:userId-count 形成redis中,设置失效时间
                    key+=":"+user.getId();
                }
                ValueOperations valueOperations = redisTemplate.opsForValue();
                Integer count = ((Integer) valueOperations.get(key));
                if(count==null){
                    valueOperations.set(key,maxCount,second,TimeUnit.SECONDS);
                }else if(count<maxCount) {
                    valueOperations.increment(key);
                }else {
                    //超过访问次数,直接返回
                    render(response,RespBeanEnum.ACCESS_LIMIT_REAHCED);
                    return false;
                }
            }
        }
        //访问不是方法,或者在限流的限制条件内可以访问该接口
        return true;
    }
    /*
     * @Description: 从拦截器返回错误信息(因为不是controller内所以没有responseBody,只能用请求转发了)
     */
    public void render(HttpServletResponse response,RespBeanEnum respBeanEnum) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        RespBean respBean = RespBean.error(respBeanEnum);
        out.write(new ObjectMapper().writeValueAsString(respBean));
        out.flush();
        out.close();
    }

    /*
     * @Description: 通过userService通过请求中的cookie获取当前登录用户
     */
    private TUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String userTicket = CookieUtil.getCookieValue(request, "userTicket");
        //请求中没有cookie返回空
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        //有cookie就交给userService去redis查
        return userService.getUserByCookie(userTicket,request,response);
    }

}
