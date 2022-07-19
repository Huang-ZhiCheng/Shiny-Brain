package com.hzc.vo;

import com.hzc.validator.IsMobile;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author : hzc
 * @date: 2022/4/10 - 04 - 10 - 17:01
 * @Description: com.hzc.vo
 * @version: 1.0
 */
/*
 * 接收前端用户账号密码对象
 */
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginVo() {
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public LoginVo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
}
