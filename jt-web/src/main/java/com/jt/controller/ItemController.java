package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;
import org.apache.zookeeper.Op;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//跳转商品的页面
@Controller
public class ItemController {
    //当先启动消费者时，暂时不校验生产者是否启动
    @Reference(check = false)
    private DubboItemService dubboItemService;

    /*
     * 实现商品页面跳转
     * url地址:   http://www.jt.com/items/562379.html
     * 参数:      商品ID: 562379
     * 返回值:    String: item
     * 页面取值:
     *          ${item.title }
     *          ${itemDesc.itemDesc }
     */

    @RequestMapping("/items/{itemId}")
    public String findItemById(@PathVariable long itemId, Model model)
    {
        Item item = dubboItemService.findItemById(itemId);
        ItemDesc itemDesc = dubboItemService.findItemDescById(itemId);
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";
    }


}
