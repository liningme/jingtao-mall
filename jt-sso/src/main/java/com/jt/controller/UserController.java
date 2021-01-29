package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.jt.service.UserService;
import redis.clients.jedis.JedisCluster;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;

	@RequestMapping("/findUserList")
	public List<User> findAll()
	{
		return userService.findAll();
	}

	/*
	 * 利用跨域实现JSONP请求
	 * URL地址:http://sso.jt.com/user/query/b3f042f1e29742ac8fe32655fa99d529?callback=jsonp1610592704700&_=1610592704737
	 * 参数:  ticket信息
	 * 返回值结果:  callback(SysResult对象)
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback)
	{
		String json = jedisCluster.get(ticket);
		if (StringUtils.hasLength(json))
		{
			return new JSONPObject(callback,SysResult.success(json));
		}
		else
		{
			//缓存中没有数据
			return new JSONPObject(callback,SysResult.fail() );
		}

	}

	/*
	 * 完成用户信息校验
	 * 1.url地址:  http://sso.jt.com/user/check/admin123/1?r=0.42106413182610014&callback=jsonp1610417332565&_=1
	 * 2.请求参数: {检验的数据}/{校验的类型}
	 * 3.返回值结果: callback(json)  JSONPObject
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type, String callback)
	{
		//查询用户数据是否存在
		boolean flag = userService.checkUser(param,type);
		//true表示数据已经存在    false  数据不存在
		SysResult sysResult = SysResult.success(flag);
		return new JSONPObject(callback,sysResult);

	}






}
