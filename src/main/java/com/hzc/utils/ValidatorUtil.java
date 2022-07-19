package com.hzc.utils;

import org.mybatis.spring.annotation.MapperScan;
import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : hzc
 * @date: 2022/4/10 - 04 - 10 - 17:20
 * @Description: com.hzc.utils
 * @version: 1.0
 */
public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
