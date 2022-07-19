package com.hzc.service.impl;

import com.hzc.pojo.TGoods;
import com.hzc.mapper.TGoodsMapper;
import com.hzc.service.TGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzc.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzc
 * @since 2022-04-13
 */
/*
 * @Description:货物Service类
 */
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements TGoodsService {
    @Autowired
    private TGoodsMapper tGoodsMapper;
    /*
     * @Description:查询出所有商品
     */
    @Override
    public List<GoodsVo> findGoodsVo() {
        return tGoodsMapper.findGoodsVo();
    }
    /*
     * @Description:查询指定id的货物
     */
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return tGoodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
