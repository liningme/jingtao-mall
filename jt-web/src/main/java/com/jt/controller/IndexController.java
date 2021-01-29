package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Retention;

@Controller
public class IndexController {
    /**
     *  1.springmvc中默认拦截的都是前缀型请求.  http://www.jt.com/index    视图解析器
     *  2.springMVC中如果遇到后缀型请求不拦截   http://www.jt.com/index.html  找具体页面
     *  3.如何实现伪静态   拦截后缀型.html请求即可!!!
     * @return
     */
    @RequestMapping("/index")
    public String index()
    {
        return "index";
    }
}
