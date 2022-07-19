package com.hzc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzc.pojo.TSeckillOrder;
import com.hzc.mapper.TSeckillOrderMapper;
import com.hzc.pojo.TUser;
import com.hzc.service.TSeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzc
 * @since 2022-04-13
 */
/*
 * @Description:秒杀订单Service类
 */
@Service
public class TSeckillOrderServiceImpl extends ServiceImpl<TSeckillOrderMapper, TSeckillOrder> implements TSeckillOrderService {
    @Autowired
    private TSeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /*
     * @Description: 成功, -1就是秒杀失败，0就是排队中
     */
    @Override
    public Long getResult(TUser user, Long goodsId) {
        TSeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<TSeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_id", goodsId)
        );
        if(seckillOrder!=null){
            return seckillOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        }else {
            return 0L;
        }

    }

}
