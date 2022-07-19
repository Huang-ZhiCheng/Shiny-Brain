package com.hzc.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @author : hzc
 * @date: 2022/4/10 - 04 - 10 - 15:36
 * @Description: com.hzc.seckill.utils
 * @version: 1.0
 */
@Component
public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    private static final String salt = "1a2b3c4d";
    public static String inputPassToFromPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass,String salt){
        //为什么还需要盐,二次加密的盐是随机的
        String str = ""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    /*
     * @Description: 这才是后端传数据入数据库真正调用的办法,其他方法都是封装
     * @param inputPass:
     * @param salt:
     * @return: java.lang.String
     */
    public static String inputPassToDBPass(String inputPass,String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = formPassToDBPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        //第一次加密的密码
        System.out.println(inputPassToFromPass("123456"));
        //第二次加密的密码 d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));
        //真正调用的办法
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
