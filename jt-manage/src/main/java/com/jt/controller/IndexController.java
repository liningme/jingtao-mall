package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	/**
	 * url1: /page/item-add     item-add.jsp
	 * url2: /page/item-list    item-list.jsp.....
	 * 需求: 能否利用一个方法实现页面的通用的跳转
	 * 解决方案: 动态的接收url中的参数  restFul
	 * restFul 语法一:
	 * 		1.参数与参数之间使用/分割
	 * 		2.参数使用{}进行包裹
	 * 		3.使用特定的注解进行接收
	 *
	 * RestFul风格语法二
	 * 		利用请求的类型实现业务识别
	 * 		1. GET    查询业务
	 * 		2. POST   新增业务
	 * 		3. PUT    修改业务
	 * 		4. DELETE 删除业务
	 */
	//@RequestMapping(value = "/page/{moduleName}",method = RequestMethod.GET)

	@RequestMapping("page/{moduleName}")
	public String modelName(@PathVariable String moduleName)
	{
		return moduleName;
	}

}
