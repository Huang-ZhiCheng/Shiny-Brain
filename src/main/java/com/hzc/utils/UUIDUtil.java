package com.hzc.utils;

import java.util.UUID;

/**
 * @author : hzc
 * @date: 2022/4/11 - 04 - 11 - 15:11
 * @Description: com.hzc.utils
 * @version: 1.0
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");//生成的短-去掉
    }

}
