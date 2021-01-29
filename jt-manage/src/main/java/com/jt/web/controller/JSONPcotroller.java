package com.jt.web.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONPcotroller {



    /*
     * 接收jt-web的请求
     *  url地址:http://manage.jt.com/web/testJSONP?callback=jQuery111109613181307511978_1610351841976&_=1610351841977
     *  参数:   回调函数
     *  返回值结果: callback(json)
     */

    /*@RequestMapping("/web/testJSONP")
    public String jsonp(String callback)
    {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("Desc 属性");
        String json = ObjectMapperUtil.toJSON(itemDesc);
        return callback + "(" + json + ")";

    }*/

    /*//jsonp的高级api
    @RequestMapping("/web/testJSONP")
    public JSONPObject jsonpAdvanced(String callback)
    {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("Desc 属性");
        return new JSONPObject(callback,itemDesc);

    }*/

    //CORS
    @CrossOrigin(value = "http:www.jt.com",methods = {RequestMethod.GET,RequestMethod.POST})
    @RequestMapping("/web/testJSONP")
    public JSONPObject jsonpAdvanced(String callback)
    {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("Desc 属性");
        return new JSONPObject(callback,itemDesc);

    }

}
