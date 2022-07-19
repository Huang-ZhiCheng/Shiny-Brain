package com.hzc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.hzc.mapper")
public class SeckillDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }
}
