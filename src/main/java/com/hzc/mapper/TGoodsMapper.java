package com.hzc.mapper;

import com.hzc.pojo.TGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzc.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hzc
 * @since 2022-04-13
 */
public interface TGoodsMapper extends BaseMapper<TGoods> {
    /*
     * @Description:查到商品列表
     * @return: java.util.List<com.hzc.vo.GoodsVo>
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(@Param("goodsId") Long goodsId);
}
