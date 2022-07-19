package com.hzc.vo;

import com.hzc.pojo.TGoods;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : hzc
 * @date: 2022/4/13 - 04 - 13 - 14:19
 * @Description: com.hzc.vo
 * @version: 1.0
 */
/*
 * @Description:商品详情类(包括商品id,秒杀开始时间等)
 */
public class GoodsVo extends TGoods {
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 库存数量
     */
    private Integer stockCount;
    /**
     * 秒杀开始时间
     */
    private Date  startDate;
    /**
     * 秒杀结束时间
     */
    private Date endDate;

    @Override
    public String toString() {
        return "GoodsVo{" +
                "seckillPrice=" + seckillPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public GoodsVo(BigDecimal seckillPrice, Integer stockCount, Date startDate, Date endDate) {
        this.seckillPrice = seckillPrice;
        this.stockCount = stockCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public GoodsVo() {
    }

    public BigDecimal getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(BigDecimal seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
