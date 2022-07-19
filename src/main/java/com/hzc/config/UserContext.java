package com.hzc.config;

import com.hzc.pojo.TUser;

/**
 * @author : hzc
 * @date: 2022/4/19 - 04 - 19 - 19:24
 * @Description: com.hzc.config
 * @version: 1.0
 */
/*
 * @Description: 每个线程存每个线程的用户对象
 */
public class UserContext {
    //解决每个线程绑定自己用户的值
    private static ThreadLocal<TUser> userHolder = new ThreadLocal<>();

    public static void setUser(TUser user){
        userHolder.set(user);
    }

    public static TUser getUser(){
        return userHolder.get();
    }
}
