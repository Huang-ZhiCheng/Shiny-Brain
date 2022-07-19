package com.hzc.service;

import com.hzc.pojo.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzc.pojo.TUser;
import com.hzc.vo.GoodsVo;
import com.hzc.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzc
 * @since 2022-04-13
 */
public interface TOrderService extends IService<TOrder> {

    TOrder seckill(TUser tUser, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    String createPath(TUser user, Long goodsId);

    boolean checkPath(TUser tUser, Long goodsId,String path);

    boolean captcha(TUser user, Long goodsId, String captcha);
}
