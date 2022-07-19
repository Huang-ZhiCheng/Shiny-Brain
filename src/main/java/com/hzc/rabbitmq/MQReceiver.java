package com.hzc.rabbitmq;

import com.hzc.pojo.SeckillMessage;
import com.hzc.pojo.TOrder;
import com.hzc.pojo.TSeckillOrder;
import com.hzc.pojo.TUser;
import com.hzc.service.TGoodsService;
import com.hzc.service.TOrderService;
import com.hzc.utils.JsonUtil;
import com.hzc.vo.GoodsVo;
import com.hzc.vo.RespBean;
import com.hzc.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : hzc
 * @date: 2022/4/18 - 04 - 18 - 16:47
 * @Description: com.hzc.rabbitmq
 * @version: 1.0
 */
/*
 * @Description:消费者类
 */
@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TOrderService orderService;

    /*
     * @Description:监听目标队列
     */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        //收到消息开始下单操作
        log.info(message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        TUser user = seckillMessage.getUser();
        //下单操作还是要判断库存
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            return;
        }
        TSeckillOrder seckillOrder = (TSeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(null!=seckillOrder){
            //该用户已经买过一件
            return;
        }
        orderService.seckill(user, goodsVo);//下单操作
    }
}
