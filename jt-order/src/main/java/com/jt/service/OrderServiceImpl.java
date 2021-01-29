package com.jt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jt.pojo.Cart;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements DubboOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;

    /*实现订单查询*/
    @Override
    public Order findOrderById(String id) {
        //1.根据orderId查询order对象
        Order order = orderMapper.selectById(id);

        //2.根据orderId查询订单物流对象
        OrderShipping orderShipping = orderShippingMapper.selectById(id);
        //3.根据orderId查询订单物流信息

        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        List<OrderItem> itemList = orderItemMapper.selectList(queryWrapper);
        order.setOrderShipping(orderShipping).setOrderItems(itemList);
        return order;
    }


    /*订单入库 保存*/
    @Transactional//控制事务
    @Override
    public String saveOrder(Order order) {
        //订单号：登录用户id+当前时间戳
        String orderId = "" + order.getUserId() + System.currentTimeMillis();

        //1.完成订单入库
        // 状态：1、未付款2、已付款3、未发货4、已发货5、交易成功6、交易关闭
        order.setOrderId(orderId).setStatus(1);
        orderMapper.insert(order);
        System.out.println("订单入库成功");

        //2.完成订单物流信息
        OrderShipping orderShipping = order.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShippingMapper.insert(orderShipping);
        System.out.println("完成订单物流入库操作");

        //3.完成订单商品入库
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
        }
        System.out.println("订单入库完成");
        return orderId;
    }


}
