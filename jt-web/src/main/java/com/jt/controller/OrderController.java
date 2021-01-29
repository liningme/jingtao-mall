package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Reference(check = false)
    private DubboOrderService dubboOrderService;
    @Reference
    private DubboCartService dubboCartService;
    /*
     * 实现订单查询
     * 1.url地址: http://www.jt.com/order/success.html?id=71610696858906
     * 2.url参数: id=71610696858906
     * 3.返回值结果: 返回成功页面
     * 4.页面取值 ${order.orderId}
     */
    @RequestMapping("/success")
    public String success(String id,Model model)
    {
        Order order = dubboOrderService.findOrderById(id);
        model.addAttribute("order",order);
        return "success";
    }


    /* 入库操作
     * URL地址:http://www.jt.com/order/submit
     * 请求参数: 整个order表单
     * 返回值结果: SysResult对象   返回orderId
     * @return
     */
    @RequestMapping("/submit")
    @ResponseBody
    public SysResult saveOrder(Order order)
    {
        Long userId = UserThreadLocal.get().getId();
        order.setUserId(userId);
        String orderId = dubboOrderService.saveOrder(order);
        return SysResult.success(orderId);
    }



    /* 订单确认页面跳转
     * url:http://www.jt.com/order/create.html
     * 参数: 暂时没有
     * 返回值: order-cart.jsp
     * 页面取值: ${carts}
     */
    @RequestMapping("/create")
    public String orderCreate(Model model)
    {
        long userId = UserThreadLocal.get().getId();
        List<Cart> carts =dubboCartService.findCartListByUserId(userId);
        model.addAttribute("carts",carts);
        return "order-cart";
    }

}
