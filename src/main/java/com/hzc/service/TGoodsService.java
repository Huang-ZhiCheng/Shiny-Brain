package com.hzc.service;

import com.hzc.pojo.TGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzc.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 * @author hzc
 * @since 2022-04-13
 */
public interface TGoodsService extends IService<TGoods> {
    /*
     * @Description:获取商品列表
     * @return: java.util.List<com.hzc.vo.GoodsVo>
     */
    List<GoodsVo> findGoodsVo();

    /*
     * @Description: 获取商品详情
     * @param goodsId:
     * @return: java.lang.String
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
