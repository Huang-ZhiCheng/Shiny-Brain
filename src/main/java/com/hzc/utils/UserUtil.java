package com.hzc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzc.pojo.TUser;
import com.hzc.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : hzc
 * @date: 2022/4/15 - 04 - 15 - 13:46
 * @Description: com.hzc.utils
 * @version: 1.0
 */
public class UserUtil {
    private static void createUser(int count) throws Exception {
        List<TUser> users = new ArrayList<>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
            TUser user = new TUser();
            user.setId(13000000000L + i);
            user.setNickname("user" + i);
            user.setSlat("1a2b3c");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSlat()));
            users.add(user);
        }
        System.out.println("create user");
          //插入数据库
//         Connection conn = getConn();
//         String sql = "insert into t_user(id, nickname, password, slat)values(?,?,?,?)";
//         PreparedStatement pstmt = conn.prepareStatement(sql);
//         for (int i = 0; i < users.size(); i++) {
//         	TUser user = users.get(i);
//         	pstmt.setObject(1, user.getId());
//         	pstmt.setObject(2, user.getNickname());
//             pstmt.setObject(3, user.getPassword());
//         	pstmt.setObject(4, user.getSlat());
//         	pstmt.addBatch();
//         }
//         pstmt.executeBatch();
//         pstmt.close();
//         conn.close();
//         System.out.println("insert to db");
        //登录，生成userTicket
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\Huang zhi cheng\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            TUser user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = ((String) respBean.getObj());
            System.out.println("create userTicket : " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://192.168.255.128:3306/seckill?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String driver ="com.mysql.cj.jdbc.Driver";
        String username = "hzc";
        String password = "root";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }

    public static void main(String[] args) throws Exception {
        createUser(20);
    }
}
