package com.hzc.service;

import com.hzc.pojo.TSeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzc.pojo.TUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzc
 * @since 2022-04-13
 */
public interface TSeckillOrderService extends IService<TSeckillOrder> {

    Long getResult(TUser user, Long goodsId);
}
