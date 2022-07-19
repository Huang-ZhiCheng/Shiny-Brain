package com.hzc.vo;

import com.hzc.pojo.TUser;

/**
 * @author : hzc
 * @date: 2022/4/16 - 04 - 16 - 14:26
 * @Description: com.hzc.vo
 * @version: 1.0
 */
/*
 * @Description:详情页面返回对象
 */
public class DetailVo {
    private TUser user;
    private GoodsVo goodsVo;
    private int secKillStatus;
    private int remainSeconds;

    public TUser getUser() {
        return user;
    }

    public void setUser(TUser user) {
        this.user = user;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public int getSecKillStatus() {
        return secKillStatus;
    }

    public void setSecKillStatus(int secKillStatus) {
        this.secKillStatus = secKillStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public DetailVo() {
    }

    public DetailVo(TUser user, GoodsVo goodsVo, int secKillStatus, int remainSeconds) {
        this.user = user;
        this.goodsVo = goodsVo;
        this.secKillStatus = secKillStatus;
        this.remainSeconds = remainSeconds;
    }

    @Override
    public String toString() {
        return "DetailVo{" +
                "user=" + user +
                ", goodsVo=" + goodsVo +
                ", secKillStatus=" + secKillStatus +
                ", remainSeconds=" + remainSeconds +
                '}';
    }

}
