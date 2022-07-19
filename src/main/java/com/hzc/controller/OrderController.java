package com.hzc.controller;


import com.hzc.pojo.TUser;
import com.hzc.service.TOrderService;
import com.hzc.vo.OrderDetailVo;
import com.hzc.vo.RespBean;
import com.hzc.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hzc
 * @since 2022-04-13
 */
/*
 * @Description:订单详情controller
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private TOrderService orderService;
    /*
     * @Description: 秒杀成功订单返回支付详情
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(TUser user,Long orderId){
        if(user==null)return RespBean.error(RespBeanEnum.SESSION_ERROR);
        OrderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }
}

