package com.hzc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzc.config.AccessLimit;
import com.hzc.exception.GlobalException;
import com.hzc.pojo.SeckillMessage;
import com.hzc.pojo.TOrder;
import com.hzc.pojo.TSeckillOrder;
import com.hzc.pojo.TUser;
import com.hzc.rabbitmq.MQSender;
import com.hzc.service.TGoodsService;
import com.hzc.service.TOrderService;
import com.hzc.service.TSeckillOrderService;
import com.hzc.utils.JsonUtil;
import com.hzc.vo.GoodsVo;
import com.hzc.vo.RespBean;
import com.hzc.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : hzc
 * @date: 2022/4/13 - 04 - 13 - 16:51
 * @Description: com.hzc.controller
 * @version: 1.0
 */
/*
 * @Description:秒杀controller
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private TSeckillOrderService tSeckillOrderService;
    @Autowired
    private TOrderService tOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    private Map<Long,Boolean> emptyStockMap = new HashMap<>();//内存标记减少多次访问redis
    @Autowired
    private RedisScript<Long> script;

    /*
     * @Description:秒杀方法接口,用了加密处理访问接口,使得不同用户的秒杀接口不一样,加密路径存入redis中
     */
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(TUser tUser, Long goodsId, @PathVariable String path){
        if(tUser==null){
            System.out.println("用户"+tUser);
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }//全部通过redis操作
        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean check = tOrderService.checkPath(tUser,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //判断该用户是否购买过一件
        TSeckillOrder seckillOrder = (TSeckillOrder)redisTemplate.opsForValue().get("order:" + tUser.getId() + ":" + goodsId);
        if(null!=seckillOrder){
            //该用户已经买过一件
            return RespBean.error(RespBeanEnum.PEPEATE_ERROR);
        }
        if(emptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);//如果此时库存数量为空
        }
        //预减库存
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);//原子性操作
        Long stock = ((Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST));
        //减完之后小于0返回
        if(stock<0){
            emptyStockMap.put(goodsId,true);//内存标记设置为空
//            valueOperations.increment("seckillGoods:" + goodsId);使用了lua脚本之后判断了小于0的情况,不用再加了
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //秒杀商品
        SeckillMessage seckillMessage = new SeckillMessage(tUser, goodsId);
        //下单走rabbitmq
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        //快速响应客户端
        return RespBean.success(0L);//为0表示正在排队中
    }
    
    /*
     * @Description: 获得秒杀结果
     * @param goodsId: 成功, -1就是秒杀失败，0就是排队中
     * @return: com.hzc.vo.RespBean
     */
    @RequestMapping(value = "/getResult",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(TUser user,Long goodsId){
        if(user==null)return RespBean.error(RespBeanEnum.SESSION_ERROR);
        Long orderId = tSeckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }
    /*
     * @Description:验证码判断,秒杀随机路径获取存入redis,并且返回前端,当前方法需要请求限流
     */
    @AccessLimit(second = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(TUser user, Long goodsId, String captcha, HttpServletRequest request){
        if(user==null)return RespBean.error(RespBeanEnum.SESSION_ERROR);
        //判断验证码是否正确
        boolean check = tOrderService.captcha(user,goodsId,captcha);
        if(!check){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = tOrderService.createPath(user,goodsId);
        return RespBean.success(str);
    }
    /*
     * @Description:返回验证码
     */
    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verifyCode(TUser user, Long goodsId, HttpServletResponse response){
        if(user==null||user.getId()<0)throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        //请求头输出为图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Param","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        //生成验证码，结果放入redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130,32,3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.info("验证码生成失败"+e.getMessage());
        }
    }

    /*
     * @Description:系统初始化,把商品库存数量加载到redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = tGoodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo -> {
            //放入redis
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(),false);//有库存改为false表示还有库存
        });
    }

}
