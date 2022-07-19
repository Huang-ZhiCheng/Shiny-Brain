package com.hzc.controller;

import com.hzc.pojo.TUser;
import com.hzc.service.TGoodsService;
import com.hzc.service.TUserService;
import com.hzc.vo.DetailVo;
import com.hzc.vo.GoodsVo;
import com.hzc.vo.RespBean;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : hzc
 * @date: 2022/4/11 - 04 - 11 - 15:34
 * @Description: com.hzc.controller
 * @version: 1.0
 */
/*
 * @Description:商品列表controller
 */
@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /*
     * @Description: 展示商品列表
     */
    @RequestMapping( value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,TUser tUser,
                         HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //Redis中获取页面
        //html页面是string类型
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //秒杀商品列表的信息
        model.addAttribute("goodsList",tGoodsService.findGoodsVo());
        model.addAttribute("user",tUser);
        //如果为空手动渲染并且把全部的文本存入redis
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if(!StringUtils.isEmpty(html)){
            //渲染不为空,全文本存入redis
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }


    /*
     * @Description: 秒杀订单详情
     */
    @RequestMapping( value = "/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(TUser tUser, @PathVariable Long goodsId){
        GoodsVo goodsVo = tGoodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatue = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀状态
        if(nowDate.before(startDate)){
            //秒杀未开始.才要倒计时
            remainSeconds = (int)((startDate.getTime()-nowDate.getTime())/1000);
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatue = 2;
            remainSeconds = -1;
        }else {
            //秒杀中
            secKillStatue = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(tUser);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatue);
        detailVo.setRemainSeconds(remainSeconds);
        //从之前的请求域改成数据,分离成了前端后端两个项目
        return RespBean.success(detailVo);
    }
}
