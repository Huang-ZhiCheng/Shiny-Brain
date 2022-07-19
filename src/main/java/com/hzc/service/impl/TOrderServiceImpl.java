package com.hzc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.hzc.exception.GlobalException;
import com.hzc.pojo.TOrder;
import com.hzc.mapper.TOrderMapper;
import com.hzc.pojo.TSeckillGoods;
import com.hzc.pojo.TSeckillOrder;
import com.hzc.pojo.TUser;
import com.hzc.service.TGoodsService;
import com.hzc.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzc.service.TSeckillGoodsService;
import com.hzc.service.TSeckillOrderService;
import com.hzc.utils.MD5Util;
import com.hzc.utils.UUIDUtil;
import com.hzc.vo.GoodsVo;
import com.hzc.vo.OrderDetailVo;
import com.hzc.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzc
 * @since 2022-04-13
 */
/*
 * @Description:订单Service类
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {
    @Autowired
    private TSeckillGoodsService tSeckillGoodsService;
    @Autowired
    private TOrderMapper tOrderMapper;
    @Autowired
    private TSeckillOrderService tSeckillOrderService;
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    /*
     * @Description:秒杀实现
     */
    @Transactional
    @Override
    public TOrder seckill(TUser tUser, GoodsVo goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        TSeckillGoods seckillGoods = tSeckillGoodsService.getOne( //先插入一张秒杀表对秒杀商品表进行操作
                new QueryWrapper<TSeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        boolean result = tSeckillGoodsService.update(new UpdateWrapper<TSeckillGoods>()
                .setSql("stock_count = stock_count -1")
                .eq("goods_id", seckillGoods.getId())
                .gt("stock_count",0)
        );
        if(seckillGoods.getStockCount()<1){
            //判断是否有库存
            valueOperations.set("isStockEmpty:"+goods.getId(),"0");
            return null;//秒杀失败(更新失败)
        }
        TOrder order = new TOrder();//插入了一个秒杀订单后对普通订单表进行操作
        order.setUserId(tUser.getId());order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);order.setStatus(1);
        order.setCreateDate(new Date());
        //生成订单
        tOrderMapper.insert(order);
        //生成秒杀订单,有一个orderId是和普通订单id相关联的
        TSeckillOrder seckillOrder = new TSeckillOrder();
        seckillOrder.setUserId(tUser.getId());seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        tSeckillOrderService.save(seckillOrder);
        //在redis中插入用户id和秒杀商品id组成的键值,以及秒杀订单对象
        redisTemplate.opsForValue().set("order:"+tUser.getId()+":"+goods.getId(),seckillOrder);
        return order;
    }

    /*
     * 查出指定订单的详情
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId==null)throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        TOrder order = tOrderMapper.selectById(orderId);
        GoodsVo goodsVo = tGoodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo(order,goodsVo);
        return detail;
    }

    /*
     * @Description: 不同用户不同id获取秒杀地址存放到redis中并访问秒杀接口
     */
    @Override
    public String createPath(TUser user, Long goodsId) {
        //校验的地址存放在redis
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,str,60, TimeUnit.SECONDS);
        return str;
    }

    /*
     * @Description: 在redis中校验秒杀地址
     */
    @Override
    public boolean checkPath(TUser tUser, Long goodsId,String path) {
        System.out.println("chekcpath");
        if(tUser==null|| StringUtils.isEmpty(path)||goodsId<0) return false;
        String str = (String)redisTemplate.opsForValue().get("seckillPath:" + tUser.getId() + ":" + goodsId);
        return path.equals(str);
    }

    /*
     * @Description: 在redis中校验验证码
     */
    @Override
    public boolean captcha(TUser user, Long goodsId, String captcha) {
        if(user==null||StringUtils.isEmpty(captcha))return false;
        String redisCaptcha = (String)redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
