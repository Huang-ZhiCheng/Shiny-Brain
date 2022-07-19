package com.hzc;

import com.hzc.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript redisScript;

    @Test
    public void testLock01(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        //如果key存在则设置不成功,占位
        if(isLock){
            valueOperations.set("name","xxxx");
            String str = (String)valueOperations.get("name");
            System.out.println(str);
            //要是在释放锁之前报了异常?--->不会删锁
//            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程正在使用,请稍后");
        }

    }

    @Test
    public void testLock02(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1",5, TimeUnit.SECONDS);
        //超时时间,挂掉了还是能正常释放锁
        // 但是一个线程要是没在时间内完成操作(网络波动)再有线程进来就会出现上一个线程的释放了下一个线程的锁
        //如果key存在则设置不成功,占位
        if(isLock){
            valueOperations.set("name","xxxx");
            String str = (String)valueOperations.get("name");
            System.out.println(str);
            //要是在释放锁之前报了异常?--->不会删锁
//            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程正在使用,请稍后");
        }
    }
    @Test
    public void test03(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = UUIDUtil.uuid();
        Boolean isLock = valueOperations.setIfAbsent("k1", value,220,TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","xxx");
            String str = (String) valueOperations.get("name");
            System.out.println(str+"   "+valueOperations.get("k1"));
            //lua脚本可以判断k-v是不是都是自己的,如果都是自己的就释放,即使是自己超时了也不应该释放别人的锁
            Boolean result = (Boolean) redisTemplate.execute(redisScript, Collections.singletonList("k1"), value);
            System.out.println(result);
        }else {
            System.out.println("有线程在使用");
        }
    }
}
