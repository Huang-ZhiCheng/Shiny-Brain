package com.hzc.vo;

import com.hzc.pojo.TOrder;

/**
 * @author : hzc
 * @date: 2022/4/16 - 04 - 16 - 16:19
 * @Description: com.hzc.vo
 * @version: 1.0
 */
/*
 * 订单详情返回对象
 */
public class OrderDetailVo {
    private TOrder order;
    private GoodsVo goodsVo;

    public OrderDetailVo() {
    }

    public OrderDetailVo(TOrder order, GoodsVo goodsVo) {
        this.order = order;
        this.goodsVo = goodsVo;
    }

    public TOrder getOrder(){
        return order;
    }

    public void setOrder(TOrder order) {
        this.order = order;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }
}
