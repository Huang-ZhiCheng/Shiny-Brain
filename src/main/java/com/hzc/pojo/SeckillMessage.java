package com.hzc.pojo;

/**
 * @author : hzc
 * @date: 2022/4/18 - 04 - 18 - 20:13
 * @Description: com.hzc.pojo
 * @version: 1.0
 */
/*
 * @Description:秒杀订单信息
 */
public class SeckillMessage {
    private TUser user;
    private Long goodsId;

    public TUser getUser() {
        return user;
    }

    public void setUser(TUser user) {
        this.user = user;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public SeckillMessage() {
    }

    public SeckillMessage(TUser user, Long goodsId) {
        this.user = user;
        this.goodsId = goodsId;
    }
}
